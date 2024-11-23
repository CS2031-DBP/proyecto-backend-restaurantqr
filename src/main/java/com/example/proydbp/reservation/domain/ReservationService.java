package com.example.proydbp.reservation.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.events.email_event.ReservaCrearEvent;
import com.example.proydbp.events.email_event.ReservaEstadoChangeEvent;
import com.example.proydbp.events.email_event.ReservaUpdateEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.reservation.dto.ReservationRequestDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MesaRepository mesaRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ClientRepository clientRepository;
    private final AuthorizationUtils authorizationUtils;
    private final MesaService mesaService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              MesaRepository mesaRepository,
                              AuthorizationUtils authorizationUtils,
                              ModelMapper modelMapper,
                              ApplicationEventPublisher eventPublisher, ClientRepository clientRepository, MesaService mesaService) {
        this.reservationRepository = reservationRepository;
        this.authorizationUtils = authorizationUtils;
        this.mesaRepository = mesaRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.clientRepository = clientRepository;
        this.mesaService = mesaService;
    }

    public Page<ReservationResponseDto> findAllReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Crear el objeto Pageable con los parámetros de página y tamaño

        // Obtener las reservas de forma paginada
        Page<Reservation> reservationsPage = reservationRepository.findAll(pageable);

        // Convertir las reservas a ReservationResponseDto
        return reservationsPage.map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class));
    }

    public ReservationResponseDto findReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con id " + id + " no encontrada"));

        return modelMapper.map(reservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto) {

        if (reservationRequestDto.getNpersonas() <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor a 0");
        }

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con nombre de usuario " + username + " no encontrado"));

        Mesa mesa = mesaRepository.findById(reservationRequestDto.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con id " + reservationRequestDto.getMesaId() + " no encontrada"));

        // Validar disponibilidad de la mesa
        ZonedDateTime reservaInicio = reservationRequestDto.getFecha();
        ZonedDateTime reservaFin = reservaInicio.plusHours(2);  // Añadimos 2 horas a la fecha de la reserva

        List<Reservation> existingReservations = reservationRepository.findByMesaAndFechaBetween(
                mesa, reservaInicio, reservaFin
        );

        if (!existingReservations.isEmpty()) {
            throw new IllegalArgumentException("La mesa ya está reservada en este horario o dentro de un rango de 2 horas.");
        }

        // Cambiar el estado de la mesa a No disponible 2 horas antes de la fecha de la reserva
        ZonedDateTime estadoCambio = reservaInicio.minusHours(2);  // 2 horas antes de la reservación
        mesa.setAvailable(false);
        mesaRepository.save(mesa);

        // Crear la nueva reservación
        Reservation newReservation = modelMapper.map(reservationRequestDto, Reservation.class);
        newReservation.setStatusReservation(StatusReservation.PENDIENTE);
        newReservation.setMesa(mesa);
        newReservation.setClient(client);

        Reservation savedReservation = reservationRepository.save(newReservation);

        // Publicar el evento de creación de reserva
        eventPublisher.publishEvent(new ReservaCrearEvent(savedReservation, client.getEmail()));

        return modelMapper.map(savedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con ID " + id + " no encontrada"));

        // Si se proporciona un nuevo ID de mesa, lo actualizamos
        if (reservationRequestDto.getMesaId() != null) {
            Mesa mesa = mesaRepository.findById(reservationRequestDto.getMesaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mesa con ID " + reservationRequestDto.getMesaId() + " no encontrada"));

            ZonedDateTime reservaInicio = reservationRequestDto.getFecha();
            ZonedDateTime reservaFin = reservaInicio.plusHours(2);  // Añadimos 2 horas a la fecha de la reserva

            // Verificar si ya hay reservas en ese horario para la mesa
            List<Reservation> existingReservations = reservationRepository.findByMesaAndFechaBetween(
                    mesa, reservaInicio, reservaFin
            );

            // Si existen reservas solapadas, lanzamos una excepción
            if (!existingReservations.isEmpty()) {
                throw new IllegalArgumentException("La mesa ya está reservada en este horario o dentro de un rango de 2 horas.");
            }

            existingReservation.setMesa(mesa);
        }

        if (reservationRequestDto.getFecha() != null) {
            existingReservation.setFecha(reservationRequestDto.getFecha());
        }

        if (reservationRequestDto.getDescripcion() != null) {
            existingReservation.setDescripcion(reservationRequestDto.getDescripcion());
        }

        // Si se proporciona un nuevo número de personas, lo actualizamos
        if (reservationRequestDto.getNpersonas() != null) {
            if (reservationRequestDto.getNpersonas() <= 0) {
                throw new IllegalArgumentException("El número de personas debe ser mayor a 0");
            }

            // Verificar si el nuevo número de personas cabe en la mesa
            if (reservationRequestDto.getNpersonas() > existingReservation.getMesa().getCapacity()) {
                throw new IllegalArgumentException("El número de personas excede la capacidad de la mesa.");
            }
            existingReservation.setNpersonas(reservationRequestDto.getNpersonas());
        }

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        eventPublisher.publishEvent(new ReservaUpdateEvent(updatedReservation, existingReservation.getClient().getEmail()));
        return modelMapper.map(updatedReservation, ReservationResponseDto.class);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con id " + id + " no encontrada"));

        reservationRepository.delete(reservation);
    }

    public ReservationResponseDto canceledReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con id " + id + " no encontrada"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, reservation.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        reservation.setStatusReservation(StatusReservation.CANCELADO);

        Reservation canceledReservation = reservationRepository.save(reservation);

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new ReservaEstadoChangeEvent(canceledReservation, canceledReservation.getClient().getEmail()));

        return modelMapper.map(canceledReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto finishedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con id " + id + " no encontrada"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario autenticado tiene el rol 'ROLE_MESERO'
        boolean isMesero = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MESERO"));

        if (!isMesero) {
            throw new UnauthorizeOperationException("Usuario no tiene permiso para acceder a este recurso");
        }

        reservation.setStatusReservation(StatusReservation.FINALIZADA);

        Reservation finishedReservation = reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservaEstadoChangeEvent(finishedReservation, finishedReservation.getClient().getEmail()));

        return modelMapper.map(finishedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto confirmedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservación con " + id + " no encontrada"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario autenticado tiene el rol 'ROLE_MESERO'
        boolean isMesero = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_MESERO"));

        // Si no tiene el rol de mesero, lanzar excepción
        if (!isMesero) {
            throw new UnauthorizeOperationException("Usuario no tiene permiso para acceder a este recurso");
        }

        reservation.setStatusReservation(StatusReservation.CONFIRMADO);

        Reservation confirmedReservation = reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservaEstadoChangeEvent(confirmedReservation, confirmedReservation.getClient().getEmail()));

        return modelMapper.map(confirmedReservation, ReservationResponseDto.class);
    }

}
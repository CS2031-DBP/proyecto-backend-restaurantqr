package com.example.proydbp.reservation.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.reservation.dto.ReservationRequestDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MesaRepository mesaRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ClientRepository clientRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              MesaRepository mesaRepository,
                              ModelMapper modelMapper,
                              ApplicationEventPublisher eventPublisher, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.mesaRepository = mesaRepository;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.clientRepository = clientRepository;
    }

    public List<ReservationResponseDto> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    public ReservationResponseDto findReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));
        return modelMapper.map(reservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto) {
        if (reservationRequestDto.getNumOfPeople() <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Mesa mesa = mesaRepository.findByNumero(reservationRequestDto.getTable())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDto.getTable()));

        Reservation newReservation = modelMapper.map(reservationRequestDto, Reservation.class);
        newReservation.setStatusReservation(StatusReservation.PENDIENTE);
        newReservation.setTable(mesa);

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email " + username));

        newReservation.setClient(client);

        Reservation savedReservation = reservationRepository.save(newReservation);

        // Publicar el evento de creación de reserva
        // eventPublisher.publishEvent(new ReservationCreadaEvent(savedReservation));

        return modelMapper.map(savedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        modelMapper.map(reservationRequestDto, existingReservation);

        Mesa mesa = mesaRepository.findByNumero(reservationRequestDto.getTable())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDto.getTable()));

        existingReservation.setTable(mesa);

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        // Publicar el evento de actualización de reserva
        //eventPublisher.publishEvent(new ReservationActualizadaEvent(updatedReservation));

        return modelMapper.map(updatedReservation, ReservationResponseDto.class);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        reservationRepository.delete(reservation);

        // Publicar el evento de eliminación de reserva
        //eventPublisher.publishEvent(new ReservationEliminadaEvent(reservation));
    }


    public ReservationResponseDto canceledReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals(reservation.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Client wit email" + reservation.getClient().getEmail()+" not authorized");
        }

        reservation.setStatusReservation(StatusReservation.valueOf("CANCELADO"));

        Reservation completedReservation = reservationRepository.save(reservation);

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationFinalizadaEvent(completedReservation));

        return modelMapper.map(completedReservation, ReservationResponseDto.class);
    }


    public ReservationResponseDto updateMyReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        modelMapper.map(reservationRequestDto, existingReservation);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals(existingReservation.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Client wit email" + existingReservation.getClient().getEmail()+" not authorized");
        }

        Mesa mesa = mesaRepository.findByNumero(reservationRequestDto.getTable())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDto.getTable()));

        existingReservation.setTable(mesa);

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        // Publicar el evento de actualización de reserva
        //eventPublisher.publishEvent(new ReservationActualizadaEvent(updatedReservation));

        return modelMapper.map(updatedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto finishedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals(reservation.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Client wit email" + reservation.getClient().getEmail()+" not authorized");
        }

        reservation.setStatusReservation(StatusReservation.valueOf("FINALIZADA"));

        Reservation completedReservation = reservationRepository.save(reservation);

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationFinalizadaEvent(completedReservation));

        return modelMapper.map(completedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto confirmedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!username.equals(reservation.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Client wit email" + reservation.getClient().getEmail()+" not authorized");
        }

        reservation.setStatusReservation(StatusReservation.valueOf("CONFIRMADO"));

        Reservation completedReservation = reservationRepository.save(reservation);

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationFinalizadaEvent(completedReservation));

        return modelMapper.map(completedReservation, ReservationResponseDto.class);
    }


}

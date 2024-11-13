package com.example.proydbp.reservation.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.events.email_event.*;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.reservation.dto.ReservationRequestDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              MesaRepository mesaRepository,
                              AuthorizationUtils authorizationUtils,
                              ModelMapper modelMapper,
                              ApplicationEventPublisher eventPublisher, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.authorizationUtils = authorizationUtils;
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

        if (reservationRequestDto.getNpersonas() <= 0) {
            throw new IllegalArgumentException("Number of people must be greater than 0");
        }

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email " + username));

        Mesa mesa = mesaRepository.findById(reservationRequestDto.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDto.getMesaId()));


        Reservation newReservation = modelMapper.map(reservationRequestDto, Reservation.class);
        newReservation.setStatusReservation(StatusReservation.PENDIENTE);
        newReservation.setMesa(mesa);
        newReservation.setStatusReservation(StatusReservation.PENDIENTE);
        newReservation.setClient(client);

        Reservation savedReservation = reservationRepository.save(newReservation);

        //String recipientEmail = newReservation.getClient().getEmail();
        // Publicar el evento de creación de reserva
        //eventPublisher.publishEvent(new ReservationCreatedEvent(savedReservation, recipientEmail));

        return modelMapper.map(savedReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto updateReservation(Long id, ReservationRequestDto reservationRequestDto) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        modelMapper.map(reservationRequestDto, existingReservation);

        Mesa mesa = mesaRepository.findById(reservationRequestDto.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationRequestDto.getMesaId()));

        existingReservation.setMesa(mesa);

        Reservation updatedReservation = reservationRepository.save(existingReservation);

        //String recipientEmail = updatedReservation.getClient().getEmail();
        // Publicar el evento de actualización de reserva
        //eventPublisher.publishEvent(new ReservationUpdatedEvent(updatedReservation, recipientEmail));

        return modelMapper.map(updatedReservation, ReservationResponseDto.class);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        reservationRepository.delete(reservation);

        // Publicar el evento de eliminación de reserva
       // String recipientEmail = "fernando.munoz.p@utec.edu.pe";
       // eventPublisher.publishEvent(new ReservationDeletedEvent(reservation, recipientEmail));
    }


    public ReservationResponseDto canceledReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, reservation.getClient().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");}
        reservation.setStatusReservation(StatusReservation.CANCELADO);

        Reservation canceledReservation = reservationRepository.save(reservation);

        String recipientEmail = canceledReservation.getClient().getEmail();

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationCanceladoEvent(canceledReservation, recipientEmail));

        return modelMapper.map(canceledReservation, ReservationResponseDto.class);
    }


    public ReservationResponseDto finishedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, reservation.getClient().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");}
        reservation.setStatusReservation(StatusReservation.FINALIZADA);

        Reservation canceledReservation = reservationRepository.save(reservation);

        String recipientEmail = canceledReservation.getClient().getEmail();

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationCanceladoEvent(canceledReservation, recipientEmail));

        return modelMapper.map(canceledReservation, ReservationResponseDto.class);
    }

    public ReservationResponseDto confirmedReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, reservation.getClient().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");}
        reservation.setStatusReservation(StatusReservation.CONFIRMADO);

        Reservation canceledReservation = reservationRepository.save(reservation);

        String recipientEmail = canceledReservation.getClient().getEmail();

        // Publicar el evento de finalización de reserva
        //eventPublisher.publishEvent(new ReservationCanceladoEvent(canceledReservation, recipientEmail));

        return modelMapper.map(canceledReservation, ReservationResponseDto.class);
    }










}

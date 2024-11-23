package com.example.proydbp.reservation.application;

import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.reservation.domain.ReservationService;
import com.example.proydbp.reservation.dto.ReservationRequestDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    final private ReservationService reservationService;
    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);


    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        ReservationResponseDto reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<?> createReservation(@Validated @RequestBody ReservationRequestDto reservationRequestDto) {
        System.out.println("reservationRequestDto: " + reservationRequestDto);
        ReservationResponseDto newReservation = reservationService.createReservation(reservationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id,
                                                                    @Validated @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto updatedReservation = reservationService.updateReservation(id, reservationRequestDto);
        return ResponseEntity.ok(updatedReservation);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PatchMapping("/{id}/cancelado")
    public ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable Long id) {
        ReservationResponseDto canceledReservation = reservationService.canceledReservation(id);
        return ResponseEntity.ok(canceledReservation);
    }

    @PreAuthorize("hasRole('ROLE_MESERO')")
    @PatchMapping("/{id}/confirmado")
    public ResponseEntity<ReservationResponseDto> confirmReservation(@PathVariable Long id) {
        ReservationResponseDto confirmedReservation = reservationService.confirmedReservation(id);
        return ResponseEntity.ok(confirmedReservation);
    }

    @PreAuthorize("hasRole('ROLE_MESERO')")
    @PatchMapping("/{id}/finalizado")
    public ResponseEntity<ReservationResponseDto> finishReservation(@PathVariable Long id) {
        ReservationResponseDto finishedReservation = reservationService.finishedReservation(id);
        return ResponseEntity.ok(finishedReservation);
    }



    //PAginacion

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }






}

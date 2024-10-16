package com.example.proydbp.reservation.application;

import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.reservation.domain.ReservationService;
import com.example.proydbp.reservation.dto.ReservationRequestDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    final private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.findAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Long id) {
        ReservationResponseDto reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@Validated @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto newReservation = reservationService.createReservation(reservationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id,
                                                                    @Validated @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto updatedReservation = reservationService.updateReservation(id, reservationRequestDto);
        return ResponseEntity.ok(updatedReservation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDto> cancelReservation(@PathVariable Long id) {
        ReservationResponseDto canceledReservation = reservationService.canceledReservation(id);
        return ResponseEntity.ok(canceledReservation);
    }

    @PreAuthorize("hasRole('MESERO')")
    @PutMapping("/{id}/confirm")
    public ResponseEntity<ReservationResponseDto> confirmReservation(@PathVariable Long id) {
        ReservationResponseDto confirmedReservation = reservationService.confirmedReservation(id);
        return ResponseEntity.ok(confirmedReservation);
    }

    @PreAuthorize("hasRole('MESERO')")
    @PutMapping("/{id}/finish")
    public ResponseEntity<ReservationResponseDto> finishReservation(@PathVariable Long id) {
        ReservationResponseDto finishedReservation = reservationService.finishedReservation(id);
        return ResponseEntity.ok(finishedReservation);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/me/{id}/editReservation")
    public ResponseEntity<ReservationResponseDto> updateMyReservation(@PathVariable Long id,
                                                                      @Validated @RequestBody ReservationRequestDto reservationRequestDto) {
        ReservationResponseDto updatedReservation = reservationService.updateMyReservation(id, reservationRequestDto);
        return ResponseEntity.ok(updatedReservation);
    }
}

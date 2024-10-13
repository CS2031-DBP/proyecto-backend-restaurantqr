package com.example.proydbp.reservation.dto;

import com.example.proydbp.reservation.domain.StatusReservation;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponseDto {

    private Long tableId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numOfPeople;
    private StatusReservation statusReservation;
    private String specialRequests;
}

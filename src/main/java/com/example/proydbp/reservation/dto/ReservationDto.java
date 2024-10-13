package com.example.proydbp.reservation.dto;

import com.example.proydbp.reservation.domain.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationDto {

    private Long tableId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private int numOfPeople;
    private Status status;
    private String specialRequests;
}

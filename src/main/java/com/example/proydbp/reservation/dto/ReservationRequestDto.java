package com.example.proydbp.reservation.dto;

import com.example.proydbp.mesa.dto.MesaResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {


    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private Integer numOfPeople;

    private Long table;

    private String specialRequests;
}

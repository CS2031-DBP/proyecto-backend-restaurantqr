package com.example.proydbp.reservation.dto;

import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private ClientResponseDto client;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private Integer numOfPeople;

    private MesaResponseDto mesa;

    @Enumerated(EnumType.STRING)
    private StatusReservation statusReservation;

    private String specialRequests;
}

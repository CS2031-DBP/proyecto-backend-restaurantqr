package com.example.proydbp.reservation.dto;

import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {
    private Long id;

    private ClientSelfResponseDto client;

    private ZonedDateTime fecha;

    private Integer Npersonas;

    private MesaResponseDto mesa;

    @Enumerated(EnumType.STRING)
    private StatusReservation statusReservation;

    private String descripcion;
}

package com.example.proydbp.reservation.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDto {


    private Long id;

    private ZonedDateTime fecha;

    private Integer Npersonas = 1;

    private Long mesaId;

    private StatusReservation statusReservation;

    private String descripcion;
}

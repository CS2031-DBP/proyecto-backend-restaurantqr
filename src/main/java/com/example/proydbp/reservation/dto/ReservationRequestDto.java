package com.example.proydbp.reservation.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    private ZonedDateTime fecha;

    @NotNull(message = "El número de personas no puede ser nulo")
    @Min(value = 1, message = "El número de personas debe ser mayor o igual a 1")
    private Integer Npersonas;

    private Long mesaId;


    private String descripcion;

}

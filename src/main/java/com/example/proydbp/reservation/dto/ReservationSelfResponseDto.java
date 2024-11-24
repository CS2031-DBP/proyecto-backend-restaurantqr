package com.example.proydbp.reservation.dto;

import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.mesa.dto.MesaSelfReponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSelfResponseDto {
    private Long id;

    private ClientSelfResponseDto client;

    private ZonedDateTime fecha;

    private Integer Npersonas;

    @Enumerated(EnumType.STRING)
    private StatusReservation statusReservation;

    private String descripcion;
}

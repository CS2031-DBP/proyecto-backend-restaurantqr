package com.example.proydbp.mesa.dto;

import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.dto.ReservationSelfResponseDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class MesaResponseDto {

    private String qr;

    private Long id;

    private Integer capacity;

    private boolean isAvailable;

    private List<ReservationSelfResponseDto> reservations;

}

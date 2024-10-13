package com.example.proydbp.reviewMesero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMeseroResponseDto {

    private Long id;

    private Long meseroId;

    private Double ratingScore;

    private Long pedidoId;

    private LocalTime hora;

    private LocalDate fecha;
}

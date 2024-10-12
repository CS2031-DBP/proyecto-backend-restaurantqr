package com.example.proydbp.reviewMesero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMeseroRequestDto {

    private Long meseroId;

    private Double ratingScore;

    private Long pedidoId;
}

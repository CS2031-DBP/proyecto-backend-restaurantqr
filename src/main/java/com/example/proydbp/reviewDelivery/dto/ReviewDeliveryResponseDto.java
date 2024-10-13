package com.example.proydbp.reviewDelivery.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeliveryResponseDto {

    private Long repartidorId;

    private Double ratingScore;

    private Long deliveryId;

    private String comment;

    private LocalTime hora;

    private LocalDate fecha;
}

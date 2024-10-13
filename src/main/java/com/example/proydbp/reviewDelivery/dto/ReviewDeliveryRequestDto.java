package com.example.proydbp.reviewDelivery.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeliveryRequestDto {


    private String emailRepartidor;

    @Column(nullable = false)
    private Double ratingScore;

    @Size(min = 0, max = 250)
    private String comment;

    private Long idDelivery;
}

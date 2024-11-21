package com.example.proydbp.reviewDelivery.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeliveryRequestDto {

    private Long repartidorId;

    private Double ratingScore;

    private String comment;

    private Long deliveryId;
}

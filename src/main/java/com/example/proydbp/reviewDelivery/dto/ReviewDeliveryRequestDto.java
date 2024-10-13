package com.example.proydbp.reviewDelivery.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeliveryRequestDto {
    private Long repartidorId;
    private Integer calificacion;
    private Long deliveryId;
    private String comentario;
}

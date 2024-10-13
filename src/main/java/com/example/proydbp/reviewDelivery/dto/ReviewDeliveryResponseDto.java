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

    private Long id;
    private Long repartidorId;
    private Integer calificacion;
    private Long deliveryId;
    private LocalTime hora;
    private LocalDate fecha;
}

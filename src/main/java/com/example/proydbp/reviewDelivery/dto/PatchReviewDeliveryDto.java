package com.example.proydbp.reviewDelivery.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.repartidor.domain.Repartidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchReviewDeliveryDto {

    @Column(nullable = false)
    private Double calificacion;

    @NotNull(message = "El comentario no puede estar vacío")
    @Size(min = 0, max = 250)
    private String comentario;

}

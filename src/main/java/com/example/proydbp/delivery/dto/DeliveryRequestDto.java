package com.example.proydbp.delivery.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryRequestDto {

    @NotNull(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "La descripción no puede estar vacía")
    private String descripcion;

    private List<Long> idProducts;

}

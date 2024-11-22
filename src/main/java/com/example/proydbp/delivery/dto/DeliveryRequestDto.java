package com.example.proydbp.delivery.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryRequestDto {

    @NotNull(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "La descripción no puede estar vacía")
    private String descripcion;

    @JsonProperty("idProducts")
    private List<Long> idProducts;

}

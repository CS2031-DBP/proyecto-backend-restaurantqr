package com.example.proydbp.delivery.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchDeliveryDto {
    @NotNull(message = "La dirección no puede estar vacía")
    @Column(length = 255)
    private String direccion;
}

package com.example.proydbp.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchOrderDto {

    @NotNull
    @Positive
    private BigDecimal price;

    @Size(min = 1, max = 250)
    private String details;
}
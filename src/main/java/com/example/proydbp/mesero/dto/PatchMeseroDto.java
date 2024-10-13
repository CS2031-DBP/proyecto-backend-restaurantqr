package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMeseroDto {

    @DecimalMax("5")
    @DecimalMin("0")
    private Double ratingScore;
}

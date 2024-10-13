package com.example.proydbp.mesa.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MesaRequestDto {

    @NotNull
    @Column(unique = true)
    private int numero;

    @Min(1)
    private int capacity;

    private boolean isAvailable;

}

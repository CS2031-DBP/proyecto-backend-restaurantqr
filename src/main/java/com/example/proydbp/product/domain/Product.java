package com.example.proydbp.product.domain;

import com.example.proydbp.client.domain.Rango;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private Boolean isAvailable;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Rango rango;
}


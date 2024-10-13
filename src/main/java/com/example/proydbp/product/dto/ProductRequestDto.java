package com.example.proydbp.product.dto;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.product.domain.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotNull
    @Size(min = 1, max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private Boolean isAvailable;

}

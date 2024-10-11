package com.example.proydbp.order.dto;

import com.example.proydbp.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    @NotNull
    private Long id;

    @NotNull
    @Positive
    private BigDecimal price;

    private List<Product> products;

    @Size(min = 1, max = 250)
    private String details;

}

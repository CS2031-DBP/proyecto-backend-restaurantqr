package com.example.proydbp.orden.dto;

import com.example.proydbp.product.dto.ProductResponseDto;
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
public class OrderRequestDto {

    private List<ProductResponseDto> products;

    @Size(min = 1, max = 250)
    private String details;
}

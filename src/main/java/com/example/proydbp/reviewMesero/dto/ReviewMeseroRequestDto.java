package com.example.proydbp.reviewMesero.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMeseroRequestDto {

    @Email
    private String emailMesero;

    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    private Double ratingScore;

    @Size(min = 0, max = 250)
    @NotEmpty
    private String comment;

    private Long idPedidoLocal;

}

package com.example.proydbp.reviewMesero.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;

import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
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

    private Long meseroId;

    private Double ratingScore;

    private String comment;

    private Long pedidoLocalId;
}

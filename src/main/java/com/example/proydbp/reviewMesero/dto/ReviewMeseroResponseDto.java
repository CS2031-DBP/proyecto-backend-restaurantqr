package com.example.proydbp.reviewMesero.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewMeseroResponseDto {

    private Long id;

    private MeseroSelfResponseDto mesero;

    private Double ratingScore;

    private String comment;

    private ClientSelfResponseDto client;

    private ZonedDateTime fecha;
}

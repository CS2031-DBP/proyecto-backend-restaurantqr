package com.example.proydbp.reviewDelivery.dto;

import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.dto.RepartidorSelfResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDeliveryResponseDto {

    private Long id;

    private RepartidorSelfResponseDto repartidor;

    private Double ratingScore;

    private String comment;

    private ClientSelfResponseDto client;

    private ZonedDateTime fecha;
}

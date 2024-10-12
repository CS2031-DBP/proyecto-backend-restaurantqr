package com.example.proydbp.pedido_local.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PedidoLocalRequestDto {

    private List<Long> ordenes;

    private Long meseroId;

    private LocalDate fecha;

    private LocalTime hora;

    private String estado;

    private Double precio;

    private String tipoPago;
}

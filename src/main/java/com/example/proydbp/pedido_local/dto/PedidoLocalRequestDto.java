package com.example.proydbp.pedido_local.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PedidoLocalRequestDto {

    private List<Long> ordenesIds;

    private Long meseroId;

    private LocalDate fecha;

    private LocalTime hora;

    private Long ordenId;

    private String estado;

    private Double precio;

    private String tipoPago;
}

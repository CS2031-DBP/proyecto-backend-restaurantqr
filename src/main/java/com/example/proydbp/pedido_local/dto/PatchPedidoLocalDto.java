package com.example.proydbp.pedido_local.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PatchPedidoLocalDto {

    private LocalDate fecha;

    private LocalTime hora;

    private String estado;

    private Double precio;

    private String tipoPago;
}

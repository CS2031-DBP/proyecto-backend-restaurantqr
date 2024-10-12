package com.example.proydbp.pedido_local.dto;

import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.orden.dto.OrderResponseDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PedidoLocalResponseDto {

    private Long id;

    private List<OrderResponseDto> ordenes;

    private MeseroResponseDto mesero;

    private LocalDate fecha;

    private LocalTime hora;

    private String estado;

    private OrderResponseDto orden;

    private Double precio;

    private String tipoPago;
}

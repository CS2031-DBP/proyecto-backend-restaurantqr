package com.example.proydbp.pedido_local.dto;

import com.example.proydbp.mesero.dto.MeseroResponseDto;

import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.domain.TipoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PedidoLocalResponseDto {

    private Long id;


    private MeseroResponseDto mesero;

    private LocalDate fecha;

    private LocalTime hora;

    private StatusPedidoLocal status;



    private Double precio;

    private TipoPago tipoPago;
}

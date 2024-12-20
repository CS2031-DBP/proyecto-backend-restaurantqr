package com.example.proydbp.pedido_local.dto;

import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.dto.MeseroResponseDto;

import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.domain.TipoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PatchPedidoLocalDto {

    private TipoPago tipoPago;
    private String descripcion;
}

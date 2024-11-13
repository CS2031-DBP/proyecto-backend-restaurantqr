package com.example.proydbp.pedido_local.dto;

import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;

import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.domain.TipoPago;
import com.example.proydbp.product.dto.ProductResponseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PedidoLocalResponseDto {

    private Long id;

    private MeseroSelfResponseDto mesero;

    private ClientSelfResponseDto client;

    private ZonedDateTime fecha;

    private MesaResponseDto mesa;

    private StatusPedidoLocal status;

    private String descripcion;

    private List<ProductResponseDto> products = new ArrayList<>();

    private Double precio;

    private TipoPago tipoPago;
}

package com.example.proydbp.delivery.dto;

import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.repartidor.dto.RepartidorSelfResponseDto;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class DeliveryResponseDto {

    private Long id;

    private ClientSelfResponseDto client;

    private String direccion;

    private Double costoDelivery;

    private ZonedDateTime fecha;

    private StatusDelivery status;

    private String descripcion;

    private List<ProductResponseDto> products = new ArrayList<>();

    private RepartidorSelfResponseDto repartidor;

    private Double precio;
}

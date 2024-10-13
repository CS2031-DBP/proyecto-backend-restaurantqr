package com.example.proydbp.delivery.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class DeliveryResponseDto {

    private ClientResponseDto client;

    @NotNull(message = "La dirección no puede estar vacía")
    @Column(length = 255)
    private String direccion;

    @NotNull(message = "El costo de la entrega no puede ser nulo")
    @Min(value = 0, message = "El costo de la entrega debe ser mayor o igual a 0")
    private Double costoDelivery;

    @NotNull(message = "La fecha de entrega no puede ser nula")
    private LocalDate fecha;

    @NotNull(message = "La hora de entrega no puede ser nula")
    private LocalTime hora;

    private StatusDelivery status;

    private OrderResponseDto order;

    private RepartidorResponseDto repartidor;

    @NotNull(message = "El precio del pedido no puede ser nulo")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Double precio;
}

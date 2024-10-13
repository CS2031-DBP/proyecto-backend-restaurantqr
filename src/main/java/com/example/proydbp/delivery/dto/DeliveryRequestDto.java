package com.example.proydbp.delivery.dto;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.repartidor.domain.Repartidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DeliveryRequestDto {

    @NotNull(message = "La dirección no puede estar vacía")
    @Column(length = 255)
    private String direccion;

    private OrderResponseDto order;
}

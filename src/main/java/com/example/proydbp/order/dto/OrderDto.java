package com.example.proydbp.order.dto;

import com.example.proydbp.order.domain.Type;
import com.example.proydbp.order_item.dto.OrderItemDto;
import com.example.proydbp.reservation.domain.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class OrderDto {

    private Long id;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @NotNull(message = "Order time is required")
    private LocalTime orderTime;

    @Size(min = 1, message = "Order must contain at least one item")
    private List<OrderItemDto> orderItems;

    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @NotNull(message = "Order type is required")
    private Type orderType;

    @NotNull(message = "Order status is required")
    private Status status;

    private String specialInstructions;

    private Long tableId;
}

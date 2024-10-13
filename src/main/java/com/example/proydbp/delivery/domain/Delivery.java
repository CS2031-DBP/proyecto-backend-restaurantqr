package com.example.proydbp.delivery.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.repartidor.domain.Repartidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Client client;

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

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado no puede ser nulo")
    @Column(name = "delivery_status")
    private StatusDelivery status;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> order;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = false)
    private Repartidor repartidor;

    @NotNull(message = "El precio del pedido no puede ser nulo")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Double precio;
}

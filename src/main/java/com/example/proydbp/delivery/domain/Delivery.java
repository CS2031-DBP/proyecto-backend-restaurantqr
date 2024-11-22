package com.example.proydbp.delivery.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Client client;

    private String direccion;

    private Double costoDelivery;

    private ZonedDateTime fecha;

    private StatusDelivery status;

    private String descripcion;

    @ElementCollection
    @JsonProperty("idProducts")
    private List<Long> idProducts;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = false)
    private Repartidor repartidor;

    private Double precio;
}

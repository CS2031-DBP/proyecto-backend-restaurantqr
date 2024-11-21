package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesero.domain.Mesero;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mesero_id", nullable = false)
    private Mesero mesero;

    private ZonedDateTime fecha;

    private String descripcion;

    @Column(name = "estado", nullable = false)
    private StatusPedidoLocal status;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ElementCollection
    private List<Long> idProducts;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Mesa mesa;
}

package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.order.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "pedidoLocal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mesero_id", nullable = false)
    private Mesero mesero;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "estado", nullable = false)
    private StatusPedidoLocal status;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


}

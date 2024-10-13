package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.delivery.domain.Status;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.order.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany
    @JoinColumn(name = "orden_id", nullable = false)
    private List<Order> ordenes;

    @ManyToOne
    @JoinColumn(name = "mesero_id", nullable = false)
    private Mesero mesero;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "estado", nullable = false)
    private Status status;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "tipo_pago", nullable = false)
    private String tipoPago;
}

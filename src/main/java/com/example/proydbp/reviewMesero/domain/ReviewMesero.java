package com.example.proydbp.reviewMesero.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMesero {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mesero_id", nullable = false)
    private Mesero mesero;

    @Column(nullable = false)
    private Double ratingScore;

    @Size(min = 0, max = 250)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoLocal pedidoLocal;
}

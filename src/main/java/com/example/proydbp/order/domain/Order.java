package com.example.proydbp.order.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.product.domain.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private BigDecimal price;

    private String details;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "pedido_local_id")
    private PedidoLocal pedidoLocal;

}

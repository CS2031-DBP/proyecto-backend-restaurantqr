package com.example.proydbp.orden.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @OneToMany(mappedBy = "orders")
    private List<Product> products;

    private Double price;

    private String details;

    @ManyToOne
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "pedido_local_id")
    private PedidoLocal pedidoLocal;
}

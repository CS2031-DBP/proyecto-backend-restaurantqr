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

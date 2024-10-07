package com.example.proydbp.order.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.order_item.domain.OrderItem;
import com.example.proydbp.reservation.domain.Status;
import com.example.proydbp.table.domain.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate orderDate;
    private LocalTime orderTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private Type orderType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String specialInstructions;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;
}

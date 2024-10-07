package com.example.proydbp.table.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String qrCode;
    private String location;
    private int capacity;
    private boolean isAvailable;

    // Relación con Reservation
    @OneToMany(mappedBy = "table")
    private List<Reservation> reservations;

    // Relación con Client
    @OneToMany(mappedBy = "table")
    private List<Client> clients;

    // Relación con Order
    @OneToMany(mappedBy = "table")
    private List<Order> orders;
}

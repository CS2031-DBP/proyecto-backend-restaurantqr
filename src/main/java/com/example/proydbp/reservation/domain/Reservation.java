package com.example.proydbp.reservation.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesa.domain.Mesa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private Integer numOfPeople;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Mesa table;

    @Enumerated(EnumType.STRING)
    private StatusReservation statusReservation;

    private String specialRequests;
}

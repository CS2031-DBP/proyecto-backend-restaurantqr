package com.example.proydbp.reviewDelivery.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.repartidor.domain.Repartidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "repartidor_id", nullable = false)
    private Repartidor repartidor;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private Double ratingScore;

    @Size(min = 0, max = 250)
    private String comment;

    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;
}

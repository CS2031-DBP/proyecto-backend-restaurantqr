package com.example.proydbp.mesa.domain;
import com.example.proydbp.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String qr;
    private int numero;
    private int capacity;

    private boolean available;

    // Relación con Reservas
    @OneToMany(mappedBy = "table") // Cambiado a "table" para coincidir con el campo en Reservation
    private List<Reservation> reservations;
}

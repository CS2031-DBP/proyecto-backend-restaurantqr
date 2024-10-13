package com.example.proydbp.mesa.domain;
import com.example.proydbp.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)

    private Long id;

    private String qr;

    private int numero;

    private int capacity;

    private boolean isAvailable;

    @OneToMany
    private List<Reservation> reservations;

}

package com.example.proydbp.mesa.domain;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
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

    private int capacity;

    private boolean available;

    @OneToMany(mappedBy = "mesa")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "mesa")
    private List<PedidoLocal> pedidosLocales;

}

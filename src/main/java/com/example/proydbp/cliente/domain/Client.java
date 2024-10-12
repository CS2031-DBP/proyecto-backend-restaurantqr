package com.example.proydbp.cliente.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @Column(name = "loyalty_points", columnDefinition = "int default 0")
    private int loyaltyPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private Level level;

    @OneToOne
    private ReviewMesero reviewMesero;

    @OneToMany
    private List<PedidoLocal> pedidoLocal;

    @OneToMany
    private List<Reservation> reservations;

    @OneToMany
    private List<Delivery> deliveries;

    @OneToOne
    private ReviewDelivery reviewDelivery;
}

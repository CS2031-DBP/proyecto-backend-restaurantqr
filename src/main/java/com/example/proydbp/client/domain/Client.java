package com.example.proydbp.client.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.order.domain.Order;
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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<PedidoLocal> pedidoLocales;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ReviewMesero> reviewMesero;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ReviewDelivery> reviewDelivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private Rango rango;


}

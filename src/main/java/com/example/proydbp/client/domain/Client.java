package com.example.proydbp.client.domain;

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
@Inheritance(strategy = InheritanceType.JOINED)
public class Client extends User {

    @Column(name = "loyalty_points", columnDefinition = "int default 0")
    private int loyaltyPoints;

    @OneToMany(mappedBy = "client")
    private List<PedidoLocal> pedidosLocales;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ReviewMesero> reviewMeseros;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ReviewDelivery> reviewDeliveries;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private Rango rango;


}

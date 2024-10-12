package com.example.proydbp.client.domain;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
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

    @Column(name = "preferred_contact_method")
    private String preferredContactMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_level")
    private Level level;

    @Column(name = "address")
    private String address;

    @ElementCollection
    @CollectionTable(name = "client_preferences", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "preference")
    private List<String> preferences;

    @OneToMany(mappedBy = "client")
    private List<Order> orderHistory;

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservationHistory;
}

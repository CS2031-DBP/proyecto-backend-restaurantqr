package com.example.proydbp.repartidor.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repartidor extends User {

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Delivery> deliveries;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewDelivery> reviewsRepartidor;

    @Column(name = "rating_score")
    private Double ratingScore;


}

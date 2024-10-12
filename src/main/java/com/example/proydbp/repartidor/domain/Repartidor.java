package com.example.proydbp.repartidor.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
public class Repartidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Delivery> deliverys;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewDelivery> reviewsRepartidor;

    @Column(name = "rating_score", precision = 3, scale = 2)
    private Double ratingScore;
}

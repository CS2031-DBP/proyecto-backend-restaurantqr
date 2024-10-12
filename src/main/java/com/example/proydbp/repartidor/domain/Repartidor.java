package com.example.proydbp.repartidor.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Repartidor extends User {

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

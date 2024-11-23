package com.example.proydbp.reviewDelivery.infrastructure;

import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewDeliveryRepository extends JpaRepository<ReviewDelivery, Long> {

    Optional<ReviewDelivery> findById(Long id);

    Page<ReviewDelivery> findByRepartidor(Repartidor repartidor, Pageable pageable);
}

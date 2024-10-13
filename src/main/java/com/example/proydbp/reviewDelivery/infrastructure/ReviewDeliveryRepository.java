package com.example.proydbp.reviewDelivery.infrastructure;

import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDeliveryRepository extends JpaRepository<ReviewDelivery, Long> {
}

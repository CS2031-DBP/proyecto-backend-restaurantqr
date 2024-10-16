package com.example.proydbp.delivery.infrastructure;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByStatusIn(List<StatusDelivery> enPreparacion);
}

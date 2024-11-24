package com.example.proydbp.delivery.infrastructure;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.repartidor.domain.Repartidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Page<Delivery> findByStatusIn(List<StatusDelivery> enPreparacion, Pageable pageable);

    Page<Delivery> findByRepartidorAndStatusNotIn(Repartidor repartidor, List<StatusDelivery> entregado, Pageable pageable);

    Page<Delivery> findByRepartidor(Repartidor repartidor, Pageable pageable);
}

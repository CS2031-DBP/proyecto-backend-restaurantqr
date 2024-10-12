package com.example.proydbp.orden.infrastructure;

import com.example.proydbp.orden.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

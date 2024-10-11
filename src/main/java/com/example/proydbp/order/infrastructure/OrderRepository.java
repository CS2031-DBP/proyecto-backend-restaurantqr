package com.example.proydbp.order.infrastructure;

import com.example.proydbp.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

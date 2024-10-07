package com.example.proydbp.order.infrastructure;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByOrderType(Type orderType);
}

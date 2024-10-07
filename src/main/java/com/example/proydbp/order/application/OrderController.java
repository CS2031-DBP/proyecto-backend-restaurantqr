package com.example.proydbp.order.application;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.domain.OrderService;
import com.example.proydbp.order.domain.Type;
import com.example.proydbp.order.dto.OrderDto;
import com.example.proydbp.reservation.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {
        Order createdOrder = orderService.save(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        Order updatedOrder = orderService.update(id, orderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH: Actualizar el estado del pedido
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        Order order = orderService.findById(id);
        order.setStatus(Status.valueOf(status));
        return ResponseEntity.ok(orderService.save(order));
    }

    // Obtener todos los pedidos por tipo (ON_SITE, RESERVATION, DELIVERY)
    @GetMapping("/type/{orderType}")
    public List<OrderDto> getOrdersByType(@PathVariable Type orderType) {
        return orderService.findByOrderType(orderType);
    }
}

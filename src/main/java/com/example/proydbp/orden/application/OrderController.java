package com.example.proydbp.orden.application;

import com.example.proydbp.orden.domain.OrderService;
import com.example.proydbp.orden.dto.OrderRequestDto;
import com.example.proydbp.orden.dto.OrderResponseDto;
import com.example.proydbp.orden.dto.PatchOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findOrderById(@PathVariable Long id) {
        OrderResponseDto orderResponseDto = orderService.findOrderById(id);
        return ResponseEntity.ok(orderResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAllOrders() {
        List<OrderResponseDto> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PreAuthorize("hasRole('CLIENT') or hasRole('MESERO')")
    @PostMapping("/")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(createdOrder);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody PatchOrderDto patchOrderDto) {
        OrderResponseDto updatedOrder = orderService.updateOrder(id, patchOrderDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @PreAuthorize("hasRole('CLIENT') or hasRole('MESERO')")
    @PatchMapping("/{idOrden}/{idProducto}/{cantidad}")
    public ResponseEntity<Void> addProducto(@PathVariable Long idOrden, @PathVariable Long idProducto, @PathVariable Integer cantidad) {
        orderService.addProducto(idOrden, idProducto, cantidad);
        return ResponseEntity.ok().build();
    }
}

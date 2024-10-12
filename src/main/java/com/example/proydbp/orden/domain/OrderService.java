package com.example.proydbp.orden.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.orden.dto.OrderRequestDto;
import com.example.proydbp.orden.dto.OrderResponseDto;
import com.example.proydbp.orden.dto.PatchOrderDto;
import com.example.proydbp.orden.infrastructure.OrderRepository;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final private OrderRepository orderRepository;
    final private ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    public OrderResponseDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return mapToResponseDto(order);
    }

    public List<OrderResponseDto> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        if (orderRequestDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        Order order = mapToEntity(orderRequestDto);
        orderRepository.save(order);
        return mapToResponseDto(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    public OrderResponseDto updateOrder(Long id, PatchOrderDto patchOrderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        if (patchOrderDto.getPrice() != null && patchOrderDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (patchOrderDto.getPrice() != null) {
            order.setPrice(patchOrderDto.getPrice());
        }
        if (patchOrderDto.getDetails() != null) {
            order.setDetails(patchOrderDto.getDetails());
        }
        orderRepository.save(order);
        return mapToResponseDto(order);
    }

    public void addProducto(Long idOrden, Long idProducto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Order order = orderRepository.findById(idOrden)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Product product = productRepository.findById(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        for (int i = 0; i < cantidad; i++) {
            order.getProducts().add(product);
        }

        orderRepository.save(order);
    }

    private OrderResponseDto mapToResponseDto(Order order) {
        return new OrderResponseDto(order.getId(), order.getPrice(), order.getProducts(), order.getDetails());
    }

    private Order mapToEntity(OrderRequestDto dto) {
        Order order = new Order();
        order.setPrice(dto.getPrice());
        order.setDetails(dto.getDetails());
        return order;
    }
}

package com.example.proydbp.order;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.domain.OrderService;
import com.example.proydbp.order.dto.OrderRequestDto;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderExceptionTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindOrderById_NotFound() {
        Long orderId = 999L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.findOrderById(orderId);
        });

        assertEquals("Order not found with id " + orderId, exception.getMessage());
    }

    @Test
    public void testDeleteOrder_NotFound() {
        Long orderId = 999L;

        when(orderRepository.existsById(orderId)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.deleteOrder(orderId);
        });

        assertEquals("Order not found with id " + orderId, exception.getMessage());
    }

    @Test
    public void testAddProducto_OrderNotFound() {
        Long orderId = 999L;
        Long productId = 1L;
        int quantity = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.addProducto(orderId, productId, quantity);
        });

        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    public void testAddProducto_ProductNotFound() {
        Long orderId = 1L;
        Long productId = 999L;
        int quantity = 1;

        Order order = new Order();
        order.setProducts(Collections.emptyList());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderService.addProducto(orderId, productId, quantity);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testAddProducto_InvalidQuantity() {
        Long orderId = 1L;
        Long productId = 1L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addProducto(orderId, productId, 0);
        });

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

}
package com.example.proydbp.order.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.order.dto.OrderRequestDto;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.order.dto.PatchOrderDto;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final private OrderRepository orderRepository;
    final private ProductRepository productRepository;
    final private ModelMapper modelMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    public OrderResponseDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return modelMapper.map(order, OrderResponseDto.class);
    }

    public List<OrderResponseDto> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .collect(Collectors.toList());
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = modelMapper.map(orderRequestDto, Order.class);
        order.setPrice(calcularPrecioTotal(order.getOrderId()));
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class);
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

        if (patchOrderDto.getDetails() != null) {
            order.setDetails(patchOrderDto.getDetails());
        }
        if (patchOrderDto.getProducts() != null) {
            List<Product> products = patchOrderDto.getProducts().stream()
                    .map(productDto -> modelMapper.map(productDto, Product.class))
                    .collect(Collectors.toList()); // Recoger los productos mapeados en una lista
            order.setProducts(products); // Asignar la lista de productos al pedido
            order.setPrice(calcularPrecioTotal(order.getOrderId()));
        }

        orderRepository.save(order);
        return modelMapper.map(order, OrderResponseDto.class); // Devolver el DTO de respuesta
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

        order.setPrice(calcularPrecioTotal(order.getOrderId()));
        orderRepository.save(order);
    }

    // adicional

    public double calcularPrecioTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        List<Product> productos = order.getProducts();

        // Sumar el precio de cada producto como double
        double total = productos.stream()
                .mapToDouble(product -> product.getPrice().doubleValue()) // Convertir BigDecimal a double
                .sum(); // Sumar los precios

        return total; // Retornar el precio total como double
    }

}

package com.example.proydbp.order.domain;

import com.example.proydbp.order.dto.OrderDto;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.reservation.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    final private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order save(OrderDto orderDto) {
        Order order = convertDtoToEntity(orderDto);
        return orderRepository.save(order);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    private void setOrderFields(Order existingOrder, OrderDto orderDto) {
        existingOrder.setOrderDate(orderDto.getOrderDate());
        existingOrder.setOrderTime(orderDto.getOrderTime());
        existingOrder.setTotalPrice(orderDto.getTotalPrice());
        existingOrder.setOrderType(orderDto.getOrderType());
        existingOrder.setStatus(Status.valueOf(String.valueOf(orderDto.getStatus())));
        existingOrder.setSpecialInstructions(orderDto.getSpecialInstructions());
    }

    private Order convertDtoToEntity(OrderDto orderDto) {
        Order order = new Order();
        setOrderFields(order, orderDto);
        return order;
    }

    public Order update(Long id, OrderDto orderDto) {
        Order existingOrder = findById(id);
        setOrderFields(existingOrder, orderDto);
        return orderRepository.save(existingOrder);
    }

    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    public List<OrderDto> findByOrderType(Type orderType) {
        List<Order> orders = orderRepository.findByOrderType(orderType);
        return orders.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public OrderDto convertEntityToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        setOrderFields(order, orderDto);
        return orderDto;
    }
}

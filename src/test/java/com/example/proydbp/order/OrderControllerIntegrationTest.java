package com.example.proydbp.order;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.domain.OrderService;
import com.example.proydbp.order.dto.OrderRequestDto;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.order.dto.PatchOrderDto;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository; // Asegúrate de importar el repositorio de productos

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private PedidoLocalRepository pedidoLocalRepository;

    @BeforeEach
    public void setUp() {

    }


    @WithMockUser(roles = "CLIENT") // Simula que el usuario tiene el rol CLIENT
    @Test
    public void testFindOrderById_NotFound() throws Exception {
        Long orderId = 999L;

        mockMvc.perform(get("/order/{id}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found with id " + orderId));
    }


    @WithMockUser(roles = "ADMIN")
    @Test
    public void testDeleteOrder_NotFound() throws Exception {
        // Simular un ID de orden que no existe
        Long orderId = 999L;

        mockMvc.perform(delete("/order/{id}", orderId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found with id " + orderId));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void testUpdateOrder_NotFound() throws Exception {
        Long orderId = 999L;

        mockMvc.perform(patch("/order/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"details\": \"Detalles actualizados\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found with id " + orderId));
    }

}
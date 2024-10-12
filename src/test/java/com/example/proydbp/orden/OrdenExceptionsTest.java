package com.example.proydbp.orden;

package com.example.proydbp.orden.application;

import com.example.proydbp.orden.application.OrderController;
import com.example.proydbp.orden.domain.Order;
import com.example.proydbp.orden.domain.OrderService;
import com.example.proydbp.orden.dto.OrderRequestDto;
import com.example.proydbp.orden.dto.OrderResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderRequestDto orderRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(BigDecimal.valueOf(10.00));
        orderRequestDto.setProductosIds(Collections.singletonList(1L));
        orderRequestDto.setDetails("Valid order details");
    }

    @Test
    public void testCreateOrderAndExpectCreated() throws Exception {
        OrderResponseDto createdOrder = new OrderResponseDto(1L, orderRequestDto.getPrice(), orderRequestDto.getProductosIds(), orderRequestDto.getDetails());
        when(orderService.createOrder(ArgumentMatchers.any())).thenReturn(createdOrder);

        var res = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String location = res.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf("/") + 1);

        assertNotNull(id);
        assertEquals("1", id);
        assertTrue(orderService.createOrder(orderRequestDto) != null);
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderResponseDto orderResponseDto = new OrderResponseDto(1L, orderRequestDto.getPrice(), orderRequestDto.getProductosIds(), orderRequestDto.getDetails());
        when(orderService.findOrderById(1L)).thenReturn(orderResponseDto);

        mockMvc.perform(get("/order/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(10.00))
                .andExpect(jsonPath("$.details").value("Valid order details"));
    }

    @Test
    public void testGetNotExistingOrder() throws Exception {
        when(orderService.findOrderById(99L)).thenThrow(new RuntimeException("Order not found"));

        mockMvc.perform(get("/order/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/order/{id}", 1))
                .andExpect(status().isNoContent());

        // Verificación de que el servicio ha sido llamado
        verify(orderService, times(1)).deleteOrder(1L);
    }

    @Test
    public void testUpdateOrder() throws Exception {
        OrderResponseDto updatedOrderResponseDto = new OrderResponseDto(1L, orderRequestDto.getPrice(), orderRequestDto.getProductosIds(), "Updated details");
        when(orderService.updateOrder(eq(1L), any())).thenReturn(updatedOrderResponseDto);

        mockMvc.perform(patch("/order/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details").value("Updated details"));
    }

    @Test
    public void testAddProductToOrder() throws Exception {
        doNothing().when(orderService).addProducto(1L, 2L, 3);

        mockMvc.perform(patch("/order/{idOrden}/{idProducto}/{cantidad}", 1, 2, 3))
                .andExpect(status().isOk());

        // Verificación de que el servicio ha sido llamado
        verify(orderService, times(1)).addProducto(1L, 2L, 3);
    }
}


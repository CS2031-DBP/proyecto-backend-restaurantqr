package com.example.proydbp.order;
import com.example.proydbp.order.dto.OrderRequestDto;
import com.example.proydbp.order.dto.OrderResponseDto;
import com.example.proydbp.order.dto.PatchOrderDto;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;


import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDtoTest {
    private static Validator validator;

    @Test
    void testOrderRequestDto() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setDetails("Detalles del pedido");

        assertNotNull(orderRequestDto.getDetails());
        assertEquals("Detalles del pedido", orderRequestDto.getDetails());
    }

    @Test
    void testOrderResponseDto() {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setPrice(200.0);

        assertNotNull(orderResponseDto.getPrice());
        assertEquals(200.0, orderResponseDto.getPrice());
    }

    @Test
    void testPatchOrderDto() {
        PatchOrderDto patchOrderDto = new PatchOrderDto();
        patchOrderDto.setDetails("Detalles actualizados");

        assertNotNull(patchOrderDto.getDetails());
        assertEquals("Detalles actualizados", patchOrderDto.getDetails());
    }
}

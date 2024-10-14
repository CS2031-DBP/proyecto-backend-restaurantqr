package com.example.proydbp.pedido_Local;

import com.example.proydbp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.Assert.assertEquals;

public class PedidoLocalExceptionTest {
    @Test
    public void testResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Resource not found");
        });
    }
}

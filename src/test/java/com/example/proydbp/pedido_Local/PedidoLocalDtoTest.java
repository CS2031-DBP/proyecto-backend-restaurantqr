package com.example.proydbp.pedido_Local;

import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class PedidoLocalDtoTest {
    @Test
    void testGetSetFecha() {
        PedidoLocalRequestDto dto = new PedidoLocalRequestDto();
        LocalDate fecha = LocalDate.now();

        dto.setFecha(fecha);
        assertEquals(fecha, dto.getFecha());
    }

    @Test
    void testGetSetHora() {
        PedidoLocalRequestDto dto = new PedidoLocalRequestDto();
        LocalTime hora = LocalTime.now();

        dto.setHora(hora);
        assertEquals(hora, dto.getHora());
    }
}

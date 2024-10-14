package com.example.proydbp.pedido_Local;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.PedidoLocalService;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PedidoLocalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoLocalService pedidoLocalService;

    @BeforeEach
    public void setUp() {

    }

    @WithMockUser(roles = {"MESERO"}) // o "CLIENT" dependiendo del rol necesario
    @Test
    public void testCreatePedidoLocal() throws Exception {
        String json = "{ \"fecha\": \"2024-10-10\", \"hora\": \"12:00:00\", \"tipoPago\": \"EFECTIVO\" }";
        when(pedidoLocalService.createPedidoLocal(any(PedidoLocalRequestDto.class)))
                .thenReturn(new PedidoLocalResponseDto(/* Inicializa con los valores deseados */));
        mockMvc.perform(post("/pedidolocal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()); // Espera un estado 200
    }



}

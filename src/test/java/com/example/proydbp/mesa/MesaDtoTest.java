package com.example.proydbp.mesa;


import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MesaDtoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MesaService mesaService; // Mock del servicio de mesa

    @BeforeEach
    public void setUp() {
        // Configuración previa a cada prueba si es necesario
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_InvalidCapacity() throws Exception {
        MesaRequestDto mesaRequestDto = new MesaRequestDto(0, 0, true);


        var result = mockMvc.perform(MockMvcRequestBuilders.post("/table")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_InvalidNumero() throws Exception {
        Mesa mesa = new Mesa();
        mesa.setNumero(1);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-5");
        mesa = mesaRepository.save(mesa);
        MesaRequestDto mesaRequestDto = new MesaRequestDto(1, 0, true);


        var result = mockMvc.perform(MockMvcRequestBuilders.post("/table")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());
    }



}

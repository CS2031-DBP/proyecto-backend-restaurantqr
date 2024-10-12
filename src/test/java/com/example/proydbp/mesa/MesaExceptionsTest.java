package com.example.proydbp.mesa;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MesaExceptionsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetMesaById_ThrowsResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/table/{id}", 999L)) // ID inexistente
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Mesa not found with id 999")));
    }

    @Test
    public void testCreateMesa_ThrowsIllegalArgumentExceptionOnDuplicateNumber() throws Exception {
        mesaRepository.save(new Mesa(null, "http://example.com/1", 1, 4, true)); // Mesa existente

        MesaRequestDto duplicateRequest = new MesaRequestDto(null, 1, 4, true);

        mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("La mesa con el número 1 ya existe.")));
    }

    @Test
    public void testCreateMesa_ThrowsIllegalArgumentExceptionOnInvalidCapacity() throws Exception {
        MesaRequestDto invalidCapacityRequest = new MesaRequestDto(null, 2, 0, true); // Capacidad inválida

        mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCapacityRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("La capacidad debe ser mayor o igual a 1.")));
    }

    @Test
    public void testUpdateMesa_ThrowsResourceNotFoundException() throws Exception {
        MesaRequestDto updateRequest = new MesaRequestDto(null, 2, 4, true);

        mockMvc.perform(put("/table/{id}", 999L) // ID inexistente
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Mesa not found with id 999")));
    }

    @Test
    public void testUpdateMesa_ThrowsIllegalArgumentExceptionOnDuplicateNumber() throws Exception {
        mesaRepository.save(new Mesa(null, "http://example.com/1", 1, 4, true));
        Mesa mesaToUpdate = mesaRepository.save(new Mesa(null, "http://example.com/2", 2, 6, true));

        MesaRequestDto duplicateNumberRequest = new MesaRequestDto(null, 1, 6, true); // Número duplicado

        mockMvc.perform(put("/table/{id}", mesaToUpdate.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateNumberRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("La mesa con el número 1 ya existe.")));
    }

    @Test
    public void testDeleteMesa_ThrowsResourceNotFoundException() throws Exception {
        mockMvc.perform(delete("/table/{id}", 999L)) // ID inexistente
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Mesa not found with id 999")));
    }
}



package com.example.proydbp.mesa;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MesaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTable_Success() throws Exception {
        MesaRequestDto mesaRequestDto = new MesaRequestDto(null, 1, 4, true);

        var result = mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value(1))
                .andExpect(jsonPath("$.capacity").value(4))
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf("/") + 1);

        Assertions.assertTrue(mesaRepository.existsById(Long.valueOf(id)));
    }

    @Test
    public void testCreateTable_ConflictOnDuplicateNumber() throws Exception {
        mesaRepository.save(new Mesa(null, "http://example.com/1", 1, 4, true));

        MesaRequestDto duplicateRequest = new MesaRequestDto(null, 1, 4, true);

        mockMvc.perform(post("/table")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetTableById_NotFound() throws Exception {
        mockMvc.perform(get("/table/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTable_Success() throws Exception {
        Mesa mesa = mesaRepository.save(new Mesa(null, "http://example.com/2", 2, 6, true));
        Long mesaId = mesa.getId();

        MesaRequestDto updateRequest = new MesaRequestDto(null, 3, 8, false);

        mockMvc.perform(put("/table/{id}", mesaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(3))
                .andExpect(jsonPath("$.capacity").value(8))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    public void testDeleteTable_Success() throws Exception {
        Mesa mesa = mesaRepository.save(new Mesa(null, "http://example.com/3", 3, 4, true));
        Long mesaId = mesa.getId();

        mockMvc.perform(delete("/table/{id}", mesaId))
                .andExpect(status().isNoContent());

        Optional<Mesa> deletedMesa = mesaRepository.findById(mesaId);
        Assertions.assertTrue(deletedMesa.isEmpty());
    }

    @Test
    public void testGetAvailableTables() throws Exception {
        mesaRepository.save(new Mesa(null, "http://example.com/4", 4, 2, true));
        mesaRepository.save(new Mesa(null, "http://example.com/5", 5, 6, false));

        mockMvc.perform(get("/table/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].numero").value(4))
                .andExpect(jsonPath("$.[0].available").value(true));
    }

    @Test
    public void testGetTablesByCapacity() throws Exception {
        mesaRepository.save(new Mesa(null, "http://example.com/6", 6, 2, true));
        mesaRepository.save(new Mesa(null, "http://example.com/7", 7, 4, true));

        mockMvc.perform(get("/table/capacity/{capacity}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].numero").value(6))
                .andExpect(jsonPath("$.[0].capacity").value(2));
    }
}


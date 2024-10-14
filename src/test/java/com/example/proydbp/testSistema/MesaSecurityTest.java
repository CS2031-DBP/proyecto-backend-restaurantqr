package com.example.proydbp.testSistema;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MesaSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MesaRepository mesaRepository;

    @BeforeEach
    public void setUp() {
        mesaRepository.deleteAll(); // Asegúrate de limpiar la base de datos antes de cada prueba
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllTables_Authorized() throws Exception {
        mockMvc.perform(get("/mesa") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllTables_Unauthorized() throws Exception {
        mockMvc.perform(get("/mesa") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetTableById_Authorized() throws Exception {
        Mesa mesa = new Mesa(); // Crear y guardar una mesa
        mesa.setId(1L);
        mesaRepository.save(mesa);

        mockMvc.perform(get("/mesa/" + mesa.getId()) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetTableById_Unauthorized() throws Exception {
        mockMvc.perform(get("/mesa/1") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_Authorized() throws Exception {
        MesaRequestDto mesaRequestDto = new MesaRequestDto(1, 4, true);

        mockMvc.perform(post("/mesa") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": 1, \"capacity\": 4, \"isAvailable\": true}")) // Ajusta según tu DTO
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testCreateTable_Unauthorized() throws Exception {
        mockMvc.perform(post("/mesa") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": 1, \"capacity\": 4, \"isAvailable\": true}")) // Ajusta según tu DTO
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateTable_Authorized() throws Exception {
        Mesa mesa = new Mesa(); // Crear y guardar una mesa
        mesa.setId(1L);
        mesaRepository.save(mesa);
        MesaRequestDto mesaRequestDto = new MesaRequestDto(1, 6, false);

        mockMvc.perform(patch("/mesa/" + mesa.getId()) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": 1, \"capacity\": 6, \"isAvailable\": false}")) // Ajusta según tu DTO
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateTable_Unauthorized() throws Exception {
        mockMvc.perform(patch("/mesa/1") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numero\": 1, \"capacity\": 6, \"isAvailable\": false}")) // Ajusta según tu DTO
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteTable_Authorized() throws Exception {
        Mesa mesa = new Mesa(); // Crear y guardar una mesa
        mesa.setId(1L);
        mesaRepository.save(mesa);

        mockMvc.perform(delete("/mesa/" + mesa.getId()) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteTable_Unauthorized() throws Exception {
        mockMvc.perform(delete("/mesa/1") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MESERO")
    public void testGetAvailableTables_Authorized() throws Exception {
        mockMvc.perform(get("/mesa/available") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAvailableTables_Unauthorized() throws Exception {
        mockMvc.perform(get("/mesa/available") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MESERO")
    public void testGetTablesByCapacity_Authorized() throws Exception {
        mockMvc.perform(get("/mesa/capacity/4") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetTablesByCapacity_Unauthorized() throws Exception {
        mockMvc.perform(get("/mesa/capacity/4") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MESERO")
    public void testGetReservationsDeMesa_Authorized() throws Exception {
        Mesa mesa = new Mesa(); // Crear y guardar una mesa
        mesa.setId(1L);
        mesaRepository.save(mesa);

        mockMvc.perform(get("/mesa/reservacionesMesa/" + mesa.getId()) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetReservationsDeMesa_Unauthorized() throws Exception {
        mockMvc.perform(get("/mesa/reservacionesMesa/1") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

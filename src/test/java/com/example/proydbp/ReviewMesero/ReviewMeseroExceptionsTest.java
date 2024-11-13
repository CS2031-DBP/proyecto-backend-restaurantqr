package com.example.proydbp.ReviewMesero;

import com.example.proydbp.order.domain.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewMeseroExceptionsTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/reviewmesero"; // Cambia esta URL si es necesario


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetReviewMeseroById_NotFound() throws Exception {
        // Intentar obtener una reseña con un ID que no existe
        Long nonExistentReviewId = 999L; // Asume que este ID no existe en la base de datos

        mockMvc.perform(MockMvcRequestBuilders.get("/reviewmesero/" + nonExistentReviewId) // Cambia por la URL correcta de tu endpoint
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testUpdateReviewMesero_notfound() throws Exception {
        PatchReviewMeseroDto patchDto = new PatchReviewMeseroDto( 5.0, "Excellent service!"); // Nuevos valores

        mockMvc.perform(MockMvcRequestBuilders.patch("/reviewmesero/" + 20L)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isNotFound());

    }


    @Test
    @WithMockUser(roles = "CLIENT")
    public void testDeleteReviewMesero_notfound() throws Exception {


        // Realiza la petición DELETE para eliminar la reseña
        mockMvc.perform(delete("/reviewmesero/" + 20L) // Cambia por la URL correcta de tu endpoint
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    @WithMockUser(roles = "CLIENT")
    public void testUpdateReviewMesero_badREquest() throws Exception {
        PatchReviewMeseroDto patchDto = new PatchReviewMeseroDto( 1000.0, "Excellent service!"); // Nuevos valores

        mockMvc.perform(MockMvcRequestBuilders.patch("/reviewmesero/" + 20L)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().is4xxClientError());

    }







































}

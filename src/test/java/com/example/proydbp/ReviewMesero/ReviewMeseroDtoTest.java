package com.example.proydbp.ReviewMesero;

import com.example.proydbp.reviewMesero.dto.ReviewMeseroRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewMeseroDtoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/reviewmesero"; // Cambia esta URL si es necesario

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateReviewMesero_InvalidEmail() throws Exception {
        ReviewMeseroRequestDto invalidDto = new ReviewMeseroRequestDto(
                "invalid-email",  // Email no válido
                4.5,  // Puntaje de calificación válido
                "Great service!" , 1L // Comentario válido
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateReviewMesero_NullRatingScore() throws Exception {
        ReviewMeseroRequestDto invalidDto = new ReviewMeseroRequestDto(
                "john@doe.com",  // Email válido
                null,  // Puntaje de calificación nulo (inválido)
                "Good service.", 1L  // Comentario válido
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateReviewMesero_InvalidRatingScore() throws Exception {
        ReviewMeseroRequestDto invalidDto = new ReviewMeseroRequestDto(
                "john@doe.com",  // Email válido
                -1.0,  // Puntaje de calificación inválido (negativo)
                "Good service.", 1L // Comentario válido
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateReviewMesero_CommentTooLong() throws Exception {
        String longComment = "a".repeat(251); // Comentario demasiado largo (más de 250 caracteres)

        ReviewMeseroRequestDto invalidDto = new ReviewMeseroRequestDto(
                "john@doe.com",  // Email válido
                4.5,  // Puntaje de calificación válido
                longComment , 1L // Comentario demasiado largo
        );

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }


}

package com.example.proydbp.reviewDelivery;

import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewDeliveryExceptionsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewDeliveryRepository reviewDeliveryRepository;

    @BeforeEach
    void setUp() {
        // Limpiar el repositorio antes de cada test (puedes usar mocks también si lo prefieres)
        reviewDeliveryRepository.deleteAll();
    }

    @Test
    public void shouldReturn404WhenReviewNotFound() throws Exception {
        // Intentar obtener una reseña que no existe
        mockMvc.perform(MockMvcRequestBuilders.get("/reviewDelivery/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ReviewRepartidor not found"));
    }

    @Test
    public void shouldReturn400WhenInvalidDataForUpdate() throws Exception {
        // Primero, crear una reseña de prueba para actualizar
        String createReviewJson = "{ \"emailRepartidor\": \"repartidor@example.com\", \"ratingScore\": 4.5, \"comment\": \"Buen servicio\", \"idDelivery\": 1 }";
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/reviewDelivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReviewJson))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        String reviewId = location.substring(location.lastIndexOf("/") + 1);

        // Intentar actualizar con un comentario inválido (más de 250 caracteres)
        String invalidUpdateJson = "{ \"comment\": \"" + "A".repeat(251) + "\", \"ratingScore\": 5 }";

        mockMvc.perform(MockMvcRequestBuilders.patch("/reviewDelivery/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUpdateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed for argument"));
    }

    @Test
    public void shouldReturn404WhenDeletingNonExistentReview() throws Exception {
        // Intentar eliminar una reseña que no existe
        mockMvc.perform(MockMvcRequestBuilders.delete("/reviewDelivery/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ReviewRepartidor not found"));
    }

    @Test
    public void shouldReturn404WhenUpdatingNonExistentReview() throws Exception {
        // Intentar actualizar una reseña que no existe
        String updateJson = "{ \"comment\": \"Nuevo comentario\", \"ratingScore\": 4 }";

        mockMvc.perform(MockMvcRequestBuilders.patch("/reviewDelivery/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ReviewRepartidor not found"));
    }

    @Test
    public void shouldReturnMethodNotAllowedForInvalidHttpMethod() throws Exception {
        // Intentar usar un método HTTP no permitido
        mockMvc.perform(MockMvcRequestBuilders.put("/reviewDelivery/{id}", 999L))
                .andExpect(status().isMethodNotAllowed());
    }
}

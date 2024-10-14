package com.example.proydbp.mesero;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MeserDtoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static final String BASE_URL = "/mesero";

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidFirstName() throws Exception {
        MeseroRequestDto invalidDto = new MeseroRequestDto(
                "",  // Nombre vacío (invalido)
                "Doe",
                "john@doe.com",
                "900900900",
                4.5,
                "password123"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidPhoneNumber() throws Exception {
        MeseroRequestDto invalidDto = new MeseroRequestDto(
                "John",
                "Doe",
                "john@doe.com",
                "123",  // Número de teléfono demasiado corto
                4.5,
                "password123"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidEmail() throws Exception {
        MeseroRequestDto invalidDto = new MeseroRequestDto(
                "John",
                "Doe",
                "invalid-email",  // Email no válido
                "900900900",
                4.5,
                "password123"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidPassword() throws Exception {
        MeseroRequestDto invalidDto = new MeseroRequestDto(
                "John",
                "Doe",
                "john@doe.com",
                "900900900",
                4.5,
                "123"  // Contraseña demasiado corta
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidRatingScore() throws Exception {
        MeseroRequestDto invalidDto = new MeseroRequestDto(
                "John",
                "Doe",
                "john@doe.com",
                "900900900",
                null,  // Score nulo (invalido)
                "password123"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }


}

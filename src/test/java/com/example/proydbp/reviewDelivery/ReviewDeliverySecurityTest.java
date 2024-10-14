package com.example.proydbp.reviewDelivery;

import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import com.example.proydbp.utils.Reader;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewDeliverySecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewDeliveryRepository reviewDeliveryRepository;

    @Autowired
    Reader reader;

    @BeforeEach
    public void setUp() throws Exception {
        reviewDeliveryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAllowAdminAccessToGetReviewDeliveryById() throws Exception {
        mockMvc.perform(get("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAllowAdminAccessToGetAllReviewDeliveries() throws Exception {
        mockMvc.perform(get("/reviewDelivery")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void shouldDenyClientAccessToGetReviewDeliveryById() throws Exception {
        mockMvc.perform(get("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAllowAdminToCreateReviewDelivery() throws Exception {
        String jsonContent = Reader.readJsonFile("/reviewDelivery/post.json");

        mockMvc.perform(post("/reviewDelivery")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void shouldDenyClientToCreateReviewDelivery() throws Exception {
        String jsonContent = Reader.readJsonFile("/reviewDelivery/post.json");

        mockMvc.perform(post("/reviewDelivery")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAllowAdminToDeleteReviewDelivery() throws Exception {
        mockMvc.perform(delete("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void shouldDenyClientToDeleteReviewDelivery() throws Exception {
        mockMvc.perform(delete("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldAllowAdminToUpdateReviewDelivery() throws Exception {
        String jsonContent = Reader.readJsonFile("/reviewDelivery/patch.json");

        mockMvc.perform(patch("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    public void shouldDenyClientToUpdateReviewDelivery() throws Exception {
        String jsonContent = Reader.readJsonFile("/reviewDelivery/patch.json");

        mockMvc.perform(patch("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void shouldDenyAnonymousAccessToGetReviewDelivery() throws Exception {
        mockMvc.perform(get("/reviewDelivery/{id}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

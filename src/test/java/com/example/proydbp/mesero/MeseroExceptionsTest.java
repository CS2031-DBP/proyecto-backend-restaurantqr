package com.example.proydbp.mesero;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.domain.TipoPago;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.infrastructure.ReviewMeseroRepository;
import com.example.proydbp.user.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.reflect.Array.get;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MeseroExceptionsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeseroRepository meseroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Mesero mesero; // Definido como un campo de la clase

    @BeforeEach
    public void setUp() {
        meseroRepository.deleteAll(); // Asegúrate de que no haya datos en el repositorio
        mesero = new Mesero("John", "Doe", "john@doe.com", "123456789", "password123");
        mesero.setRole(Role.MESERO);
        meseroRepository.save(mesero); // Guarda el mesero en el repositorio
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetMeseroById_NotFound() throws Exception {
        Long invalidId = 999L; // ID que no existe
        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john@doe.com", roles = "MESERO")
    public void testFindMesero_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    @WithMockUser(username = "unknown@doe.com", roles = "MESERO")
    public void testFindMesero_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Tests para encontrar todos los meseros
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAllMeseros_Success() throws Exception {
        meseroRepository.deleteAll(); // Limpiar el repositorio

        Mesero mesero1 = new Mesero("John", "Doe", "johdn@doe.com", "123456789", "password123");
        mesero1.setRole(Role.MESERO);
        Mesero mesero2 = new Mesero("John", "Doe", "john1@doe.com", "123456789", "password123");
        mesero2.setRole(Role.MESERO);
        meseroRepository.save(mesero1);
        meseroRepository.save(mesero2);

        mockMvc.perform(MockMvcRequestBuilders.get("/mesero")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    // Tests para crear mesero
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_Success() throws Exception {
        meseroRepository.deleteAll(); // Limpiar el repositorio
        MeseroRequestDto meseroDto = new MeseroRequestDto("John", "Doe", "joh5n@doe.com", "123456789", 4.5, "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/mesero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meseroDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joh5n@doe.com"));
    }

    // Tests para eliminar mesero
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteMesero_Success() throws Exception {
        Long meseroId = mesero.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/mesero/" + meseroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteMesero_NotFound() throws Exception {
        Long invalidId = 999L; // ID que no existe
        mockMvc.perform(MockMvcRequestBuilders.delete("/mesero/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidEmail() throws Exception {
        MeseroRequestDto meseroDto = new MeseroRequestDto("John", "Doe", "invalid-email", "123456789", 4.5, "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/mesero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meseroDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_InvalidPhoneNumber() throws Exception {
        MeseroRequestDto meseroDto = new MeseroRequestDto("John", "Doe", "john@doe.com", "invalid-phone", 4.5, "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/mesero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meseroDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateMesero_InvalidEmail() throws Exception {
        Long meseroId = mesero.getId();
        PatchMeseroDto patchMeseroDto = new PatchMeseroDto("Jane", "Smith", "invalid-email", "987654321", "porquasdetu");

        mockMvc.perform(MockMvcRequestBuilders.patch("/mesero/" + meseroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchMeseroDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateMesero_InvalidPhoneNumber() throws Exception {
        Long meseroId = mesero.getId();
        PatchMeseroDto patchMeseroDto = new PatchMeseroDto("Jane", "Smith", "hola@gmail.com", "invalid-phone", "porquasdetu");

        mockMvc.perform(MockMvcRequestBuilders.patch("/mesero/" + meseroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchMeseroDto)))
                .andExpect(status().isBadRequest());
    }


}


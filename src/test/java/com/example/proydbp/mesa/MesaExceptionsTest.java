package com.example.proydbp.mesa;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.user.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MesaExceptionsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testGetTableById_notFound() throws Exception {
        mesaRepository.deleteAll();

        // Realiza la solicitud GET para la mesa con ID que no existe
        mockMvc.perform(get("/table/" + 2) // Cambia "/table/" por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_NumeroExiste() throws Exception {
        mesaRepository.deleteAll();
        // Crear y guardar una mesa
        Mesa mesa = new Mesa();
        mesa.setNumero(7);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-7");
        mesa = mesaRepository.save(mesa);

        MesaRequestDto mesaRequestDto = new MesaRequestDto( 7, 4, true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/table") // Asegúrate de usar MockMvcRequestBuilders
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_BadCapacity() throws Exception {
        mesaRepository.deleteAll();

        MesaRequestDto mesaRequestDto = new MesaRequestDto( 7, 0, true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/table") // Asegúrate de usar MockMvcRequestBuilders
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());

    }


    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testUpdateTable_BadCapacity() throws Exception {
        mesaRepository.deleteAll();
        MesaRequestDto mesaRequestDto = new MesaRequestDto(8, 0, false); // Nuevos valores para la mesa
        // Crear y guardar una mesa
        Mesa mesa = new Mesa();
        mesa.setNumero(7);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-7");
        mesa = mesaRepository.save(mesa);
        String mesaId = mesa.getId().toString();

        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/table/"+mesaId) // Asegúrate de usar MockMvcRequestBuilders
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testUpdateTable_NumeroExiste() throws Exception {
        mesaRepository.deleteAll();
        MesaRequestDto mesaRequestDto = new MesaRequestDto(8, 0, false); // Nuevos valores para la mesa
        // Crear y guardar una mesa
        Mesa mesa = new Mesa();
        mesa.setNumero(7);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-7");
        mesa = mesaRepository.save(mesa);
        String mesaId = mesa.getId().toString();

        Mesa mesa2 = new Mesa();
        mesa2.setNumero(8);
        mesa2.setCapacity(4);
        mesa2.setAvailable(true);
        mesa2.setQr("qr-code-7");
        mesa2 = mesaRepository.save(mesa);

        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/table/"+mesaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testDeleteTable_NotFound() throws Exception {
        mesaRepository.deleteAll();
        mockMvc.perform(delete("/table/" + 2)) // Cambia "/table/" por la URL correcta de tu endpoint
                .andExpect(status().isNotFound()); // Verifica que la respuesta sea 204 No Content

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetReservationsDeMesa_NotFound() throws Exception {
        mesaRepository.deleteAll();


        // Realiza la solicitud GET para obtener las reservas de la mesa
        mockMvc.perform(get("/table/reservacionesMesa/"+ 2))
                .andExpect(status().isBadRequest()); // Verifica que la respuesta sea 200 OK


    }







}

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
public class MesaControllerIntegrationTest {

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

    @BeforeEach
    public void setUp() {
        mesaRepository.deleteAll();
        // Crear algunas mesas
        Mesa mesa1 = new Mesa();
        mesa1.setNumero(1);
        mesa1.setCapacity(4);
        mesa1.setAvailable(false);
        mesa1.setQr("miqr");
        mesaRepository.save(mesa1);

        Mesa mesa2 = new Mesa();
        mesa2.setNumero(2);
        mesa2.setCapacity(4);
        mesa2.setAvailable(false);
        mesa2.setQr("miqr");
        mesaRepository.save(mesa2);

        Mesa mesa3 = new Mesa();
        mesa3.setNumero(3);
        mesa3.setCapacity(2);
        mesa3.setAvailable(true);
        mesa3.setQr("miqr");
        mesaRepository.save(mesa3);

        Mesa mesa4 = new Mesa();
        mesa4.setNumero(4);
        mesa4.setCapacity(2);
        mesa4.setAvailable(true);
        mesa4.setQr("miqr");
        mesaRepository.save(mesa4);


    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testGetTableById_Success() throws Exception {
        // Realiza la petición GET para obtener una mesa por su ID

        // Crear y guardar una mesa
        Mesa mesa = new Mesa();
        mesa.setNumero(5);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-5");
        mesa = mesaRepository.save(mesa);
        String mesaId = mesa.getId().toString();

        mockMvc.perform(get("/table/" + mesaId) // Cambia "/table/" por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(5)) // Verifica el número de la mesa
                .andExpect(jsonPath("$.capacity").value(4)) // Verifica la capacidad de la mesa
                .andExpect(jsonPath("$.available").value(true)) // Verifica que la mesa esté disponible
                .andExpect(jsonPath("$.qr").value("qr-code-5")); // Verifica el código QR
    }



    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testGetAllTables_Success() throws Exception {
        // Realiza la petición GET para obtener todas las mesas
        mockMvc.perform(get("/table") // Cambia "/table" por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty()) // Verifica que la lista no esté vacía
                .andExpect(jsonPath("$[0].numero").value(1)) // Verifica el número de la primera mesa
                .andExpect(jsonPath("$[1].numero").value(2)) // Verifica el número de la segunda mesa
                .andExpect(jsonPath("$[2].numero").value(3)) // Verifica el número de la tercera mesa
                .andExpect(jsonPath("$[3].numero").value(4)); // Verifica el número de la cuarta mesa
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateTable_Success() throws Exception {
        MesaRequestDto mesaRequestDto = new MesaRequestDto( 90, 4, true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/table") // Asegúrate de usar MockMvcRequestBuilders
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mesaRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value(90))
                .andExpect(jsonPath("$.capacity").value(4))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.qr").value("https://192.168.1.100auth/login/"))
                .andReturn();

        Assertions.assertTrue(mesaRepository.existsByNumero(mesaRequestDto.getNumero()));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testUpdateTable_Success() throws Exception {
        MesaRequestDto mesaRequestDto = new MesaRequestDto(8, 6, false); // Nuevos valores para la mesa
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(8)) // Verifica el nuevo número
                .andExpect(jsonPath("$.capacity").value(6)) // Verifica la nueva capacidad
                .andExpect(jsonPath("$.available").value(false)) // Verifica que no esté disponible
                .andReturn();

        // Verifica que la mesa actualizada existe con los nuevos valores
        Assertions.assertTrue(mesaRepository.existsByNumero(mesaRequestDto.getNumero()));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol ADMIN
    public void testDeleteTable_Success() throws Exception {

        Mesa mesa = new Mesa();
        mesa.setNumero(80);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-7");
        mesa = mesaRepository.save(mesa);
        String mesaId = mesa.getId().toString();
        // Realiza la petición DELETE para eliminar la mesa
        mockMvc.perform(delete("/table/" + mesaId)) // Cambia "/table/" por la URL correcta de tu endpoint
                .andExpect(status().isNoContent()); // Verifica que la respuesta sea 204 No Content

        // Verifica que la mesa ya no exista en la base de datos
        Assertions.assertFalse(mesaRepository.existsByNumero(mesa.getNumero()));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol MESERO
    public void testGetAvailableTables_Success() throws Exception {
        setUp();
        // Realiza la solicitud GET para obtener mesas disponibles
        mockMvc.perform(get("/table/available")) // Cambia "/table/available" por la URL correcta de tu endpoint
                .andExpect(status().isOk()) // Verifica que la respuesta sea 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que hay 2 mesas disponibles
                .andExpect(jsonPath("$[0].numero").value(3)) // Verifica la primera mesa disponible
                .andExpect(jsonPath("$[1].numero").value(4)); // Verifica la segunda mesa disponible
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simula un usuario autenticado con el rol MESERO
    public void testGetTablesByCapacity_Success() throws Exception {
        int capacity = 4; // Cambia la capacidad según sea necesario
        setUp();
        // Realiza la solicitud GET para obtener mesas según la capacidad
        mockMvc.perform(get("/table/capacity/{capacity}", capacity)) // Cambia "/table/capacity" por la URL correcta de tu endpoint
                .andExpect(status().isOk()) // Verifica que la respuesta sea 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Verifica que hay 1 mesa con capacidad 4
                .andExpect(jsonPath("$[0].numero").value(1)) // Verifica el número de la mesa
                .andExpect(jsonPath("$[1].numero").value(2)); // Verifica el número de la mesa
    }

    @Test
    @WithMockUser(roles = "ADMIN") /// Simula un usuario autenticado con el rol MESERO
    public void testGetReservationsDeMesa_Success() throws Exception {
        mesaRepository.deleteAll();

        Client cliente = new Client();
        cliente.setFirstName("John");
        cliente.setLastName("Doe");
        cliente.setEmail("john@doe.com");
        cliente.setPassword("password");
        cliente.setPhoneNumber("900900900");
        cliente.setRango(Rango.BRONZE);
        cliente.setRole(Role.CLIENT);
        Client cliente2 = clientRepository.save(cliente);

        Mesa mesa = new Mesa();
        mesa.setNumero(5);
        mesa.setCapacity(4);
        mesa.setAvailable(true);
        mesa.setQr("qr-code-5");
        mesa = mesaRepository.save(mesa);



        Reservation reservation1 = new Reservation();
        reservation1.setMesa(mesa);
        reservation1.setClient(cliente2);
        reservation1.setNumOfPeople(4);
        reservation1.setReservationDate(LocalDate.of(2023, 6, 1));
        reservationRepository.save(reservation1);

        String mesaId = mesa.getId().toString();


        // Realiza la solicitud GET para obtener las reservas de la mesa
        mockMvc.perform(get("/table/reservacionesMesa/"+ mesaId))
                .andExpect(status().isOk()) // Verifica que la respuesta sea 200 OK
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].client.firstName").value("John")); // Verifica el nombre del cliente

    }



















































}
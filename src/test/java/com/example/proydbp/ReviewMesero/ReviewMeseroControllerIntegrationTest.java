package com.example.proydbp.ReviewMesero;

import com.example.proydbp.mesero.domain.Mesero;
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
import com.example.proydbp.reviewMesero.domain.ReviewMeseroService;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroRequestDto;
import com.example.proydbp.reviewMesero.infrastructure.ReviewMeseroRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.client.infrastructure.ClientRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewMeseroControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewMeseroRepository reviewMeseroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReviewMeseroService reviewMeseroService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PedidoLocalRepository pedidoLocalRepository;

    @Autowired
    private MeseroRepository meseroRepository;

    @BeforeEach
    public void setUp() {
        reviewMeseroRepository.deleteAll(); // Asegúrate de limpiar la base de datos antes de cada prueba
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetReviewMeseroById_Success() throws Exception {
        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
       mesero2.setRole(Role.MESERO);
       meseroRepository.save(mesero2);
       ReviewMesero review = crearReviewMEsero(mesero2);


        Long reviewId = review.getId(); // Obtener el ID de la reseña creada

        mockMvc.perform(get("/reviewmesero/" + reviewId) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingScore").value(4.5)) // Verifica el puntaje de calificación
                .andExpect(jsonPath("$.comment").value("nada")); // Verifica el comentario
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllReviewMeseros_Success() throws Exception {
        // Crea y guarda algunas reseñas
        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
        mesero2.setRole(Role.MESERO);
        meseroRepository.save(mesero2);
        ReviewMesero review = crearReviewMEsero(mesero2);

        Mesero mesero3 = new Mesero("John", "Doe", "john48@doe.com", "123456789", "password123");
        mesero3.setRole(Role.MESERO);
        meseroRepository.save(mesero3);
        ReviewMesero review2 = crearReviewMEsero(mesero3);


        mockMvc.perform(get("/reviewmesero") // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Verifica que haya 2 reseñas

    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testCreateReviewMesero_Success() throws Exception {

        clientRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
        pedidoLocalRepository.deleteAll();
        reviewMeseroRepository.deleteAll();

        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
        mesero2.setRole(Role.MESERO);
        meseroRepository.save(mesero2);

        // Crear y guardar el cliente
        Client cliente = new Client();
        cliente.setFirstName("John");
        cliente.setLastName("Doe");
        cliente.setEmail("john9@doe.com");
        cliente.setPassword("password");
        cliente.setPhoneNumber("900900900");
        cliente.setRango(Rango.BRONZE);
        cliente.setRole(Role.CLIENT);
        clientRepository.save(cliente);

        // Crear y guardar el producto
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);

        // Crear y guardar la orden
        Order orden = new Order();
        orden.setDetails("Con mayonesa");
        orden.setProducts(List.of(producto));
        orderRepository.save(orden);

        // Crear y guardar el pedido local
        PedidoLocal pedidoLocal = new PedidoLocal();
        pedidoLocal.setClient(cliente);
        pedidoLocal.setMesero(mesero2); // mesero ya guardado en setUp
        pedidoLocal.setOrders(List.of(orden));
        pedidoLocal.setFecha(LocalDate.now());
        pedidoLocal.setHora(LocalTime.now());
        pedidoLocal.setStatus(StatusPedidoLocal.LISTO);
        pedidoLocal.setPrecio(2.4);
        pedidoLocal.setTipoPago(TipoPago.EFECTIVO);
        pedidoLocalRepository.save(pedidoLocal);
        ReviewMeseroRequestDto requestDto = new ReviewMeseroRequestDto("john9@doe.com", 4.5, "Great service!",pedidoLocal.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/reviewmesero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ratingScore").value(4.5)) // Verifica el puntaje de calificación
                .andExpect(jsonPath("$.comment").value("nada")); // Verifica el comentario


    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testUpdateReviewMesero_Success() throws Exception {
        // Crea y guarda una reseña
        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
        mesero2.setRole(Role.MESERO);
        meseroRepository.save(mesero2);
        ReviewMesero review = crearReviewMEsero(mesero2);
        Long reviewId = review.getId();

        PatchReviewMeseroDto patchDto = new PatchReviewMeseroDto( 5.0, "Excellent service!"); // Nuevos valores

        mockMvc.perform(MockMvcRequestBuilders.patch("/reviewmesero/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ratingScore").value(5.0)) // Verifica el nuevo puntaje
                .andExpect(jsonPath("$.comment").value("Excellent service!")); // Verifica el nuevo comentario
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testDeleteReviewMesero_Success() throws Exception {

        // Crea y guarda una reseña
        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
        mesero2.setRole(Role.MESERO);
        meseroRepository.save(mesero2);
        ReviewMesero review = crearReviewMEsero(mesero2);
        Long reviewId = review.getId();

        // Realiza la petición DELETE para eliminar la reseña
        mockMvc.perform(delete("/reviewmesero/" + reviewId) // Cambia por la URL correcta de tu endpoint
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Verifica que la respuesta sea 204 No Content

        // Verifica que la reseña ya no exista en la base de datos
        Assertions.assertFalse(reviewMeseroRepository.existsById(reviewId));
    }


    public ReviewMesero crearReviewMEsero(Mesero mesero2) {
        // Eliminar solo las entidades necesarias, evita eliminar el mesero que se usa en las pruebas
        clientRepository.deleteAll();
        productRepository.deleteAll();
        orderRepository.deleteAll();
        pedidoLocalRepository.deleteAll();
        reviewMeseroRepository.deleteAll();

        // Crear y guardar el cliente
        Client cliente = new Client();
        cliente.setFirstName("John");
        cliente.setLastName("Doe");
        cliente.setEmail("john9@doe.com");
        cliente.setPassword("password");
        cliente.setPhoneNumber("900900900");
        cliente.setRango(Rango.BRONZE);
        cliente.setRole(Role.CLIENT);
        Client cliente2 = clientRepository.save(cliente);

        // Crear y guardar el producto
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);

        // Crear y guardar la orden
        Order orden = new Order();
        orden.setDetails("Con mayonesa");
        orden.setProducts(List.of(producto));
        orderRepository.save(orden);

        // Crear y guardar el pedido local
        PedidoLocal pedidoLocal = new PedidoLocal();
        pedidoLocal.setClient(cliente2);
        pedidoLocal.setMesero(mesero2); // mesero ya guardado en setUp
        pedidoLocal.setOrders(List.of(orden));
        pedidoLocal.setFecha(LocalDate.now());
        pedidoLocal.setHora(LocalTime.now());
        pedidoLocal.setStatus(StatusPedidoLocal.LISTO);
        pedidoLocal.setPrecio(2.4);
        pedidoLocal.setTipoPago(TipoPago.EFECTIVO);
        pedidoLocalRepository.save(pedidoLocal);

        // Crear y guardar la reseña del mesero
        ReviewMesero reviewMesero = new ReviewMesero();
        reviewMesero.setClient(cliente2);
        reviewMesero.setMesero(mesero2); // Referencia al mesero existente
        reviewMesero.setPedidoLocal(pedidoLocal);
        reviewMesero.setRatingScore(4.5);
        reviewMesero.setComment("nada");
        reviewMesero.setFecha(LocalDate.now());
        reviewMesero.setHora(LocalTime.now());
        reviewMeseroRepository.save(reviewMesero);

        return reviewMesero;

    }

}


package com.example.proydbp.reviewDelivery;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.order.infrastructure.OrderRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewDelivery.dto.PatchReviewDeliveryDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import com.example.proydbp.reviewDelivery.infrastructure.ReviewDeliveryRepository;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewDeliveryControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewDeliveryRepository reviewDeliveryRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reviewDeliveryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetReviewDeliveryById_Success() throws Exception {
        ReviewDelivery review = createReviewDelivery();
        Long reviewId = review.getId();

        mockMvc.perform(get("/reviewdelivery/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Muy buen servicio"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllReviewDeliveries_Success() throws Exception {
        createReviewDelivery();
        createReviewDelivery();

        mockMvc.perform(get("/reviewdelivery/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));  // Verifica que haya dos reseñas
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateReviewDelivery_Success() throws Exception {
        Delivery delivery = createDelivery();
        Repartidor repartidor = createRepartidor();

        ReviewDeliveryRequestDto reviewRequestDto = new ReviewDeliveryRequestDto(
                repartidor.getEmail(),
                5.0,
                "Excelente servicio",
                delivery.getId()
        );

        mockMvc.perform(post("/reviewdelivery/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comentario").value("Excelente servicio"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteReviewDelivery_Success() throws Exception {
        ReviewDelivery review = createReviewDelivery();
        Long reviewId = review.getId();

        mockMvc.perform(delete("/reviewdelivery/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateReviewDelivery_Success() throws Exception {
        ReviewDelivery review = createReviewDelivery();
        Long reviewId = review.getId();

        PatchReviewDeliveryDto patchReviewDto = new PatchReviewDeliveryDto(
                4.0,
                "Buena entrega"
        );

        mockMvc.perform(patch("/reviewdelivery/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchReviewDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Buena entrega"))
                .andExpect(jsonPath("$.ratingScore").value(4.0));
    }

    // Métodos auxiliares para crear objetos de prueba

    private ReviewDelivery createReviewDelivery() {
        ReviewDelivery review = new ReviewDelivery();
        review.setCalificacion(5.0);
        review.setComentario("Muy buen servicio");
        return reviewDeliveryRepository.save(review);
    }

    private Delivery createDelivery() {
        Delivery delivery = new Delivery();
        delivery.setDireccion("Calle Falsa 123");
        delivery.setCostoDelivery(5.0);
        delivery.setFecha(LocalDate.now());
        delivery.setHora(LocalTime.now());
        delivery.setStatus(StatusDelivery.RECIBIDO);
        delivery.setPrecio(50.0);
        return deliveryRepository.save(delivery);
    }

    private Repartidor createRepartidor() {
        Repartidor repartidor = new Repartidor();
        repartidor.setEmail("repartidor@example.com");
        return repartidorRepository.save(repartidor);
    }
}

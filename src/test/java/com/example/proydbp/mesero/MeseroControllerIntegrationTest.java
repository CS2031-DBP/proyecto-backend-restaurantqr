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
public class MeseroControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MeseroRepository meseroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PedidoLocalRepository pedidoLocal;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PedidoLocalRepository pedidoLocalRepository;
    @Autowired
    private ReviewMeseroRepository reviewMeseroRepository;

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
    public void testGetMeseroById_Success() throws Exception {
        Long meseroId = mesero.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/" + meseroId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@doe.com"));
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
    @WithMockUser(roles = "ADMIN")
    public void testFindAllMeseros_Success() throws Exception {
        meseroRepository.deleteAll();

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

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateMesero_Success() throws Exception {
        meseroRepository.deleteAll();
        MeseroRequestDto meseroDto = new MeseroRequestDto("John", "Doe", "joh5n@doe.com", "123456789", 4.5, "password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/mesero")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meseroDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("joh5n@doe.com"));
    }

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
    public void testUpdateMesero_Success() throws Exception {

        Long meseroId = mesero.getId();
        PatchMeseroDto patchMeseroDto = new PatchMeseroDto("Jane", "Smith","hola@gmail.com" ,"987654321","porquasdetu" );

        mockMvc.perform(MockMvcRequestBuilders.patch("/mesero/" + meseroId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchMeseroDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("hola@gmail.com"));
    }


//    @Test
//    @Transactional
//    @WithMockUser(username = "john@doe.com", roles = "MESERO")
//    public void testFindPedidosLocalesActuales_Success() throws Exception {
//        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
//        mesero2.setRole(Role.MESERO);
//        meseroRepository.save(mesero2); // Guarda el mesero en el repositorio
//
//        crearPedidoLocal(mesero2);
//        System.out.println("Mesero ID: " + mesero2.getId());
//        System.out.println(mesero2.getReviewMesero());
//        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/me/pedidosLocalesActuales")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "john8@doe.com", roles = "MESERO")
//    public void testFindMisReviews_Success() throws Exception {
//        Mesero mesero2 = new Mesero("John", "Doe", "john8@doe.com", "123456789", "password123");
//        mesero2.setRole(Role.MESERO);
//        meseroRepository.save(mesero2);
//        crearPedidoLocal(mesero2);
//        // Realizar la solicitud GET y validar la respuesta
//        mockMvc.perform(MockMvcRequestBuilders.get("/mesero/me/misReviews")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
//    }




    void crearPedidoLocal(Mesero mesero2) {
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
        reviewMesero.setRatingScore(5.0);
        reviewMesero.setComment("Muy malo");
        reviewMesero.setFecha(LocalDate.now());
        reviewMesero.setHora(LocalTime.now());
        reviewMeseroRepository.save(reviewMesero);

    }



































































}

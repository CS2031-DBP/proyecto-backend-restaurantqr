package com.example.proydbp.product;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.dto.PatchAvailability;
import com.example.proydbp.product.dto.PatchProductDto;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
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
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProductById_Success() throws Exception {

        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        productRepository.save(producto);

        Long productId = producto.getId();
        mockMvc.perform(get("/product/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Inka kola"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllProducts_Success() throws Exception {
        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);

        Product producto2 = new Product();
        producto2.setCategory(Category.APERITIVO);
        producto2.setNombre("Inka kola");
        producto2.setDescripcion("Muy rico");
        producto2.setPrice(2.0);
        producto2.setIsAvailable(true);
        productRepository.save(producto2);

        Product producto3 = new Product();
        producto3.setCategory(Category.APERITIVO);
        producto3.setNombre("Inka kola");
        producto3.setDescripcion("Muy rico");
        producto3.setPrice(2.0);
        producto3.setIsAvailable(true);
        productRepository.save(producto3);

        mockMvc.perform(get("/product/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_Success() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto("Coca","Descripcion",2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Coca"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteProduct_Success() throws Exception {

        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);

        Long productId = producto.getId(); // Suponiendo que este producto existe
        mockMvc.perform(delete("/product/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProduct_Success() throws Exception {
        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        productRepository.save(producto);
        Long productId = producto.getId();

        PatchProductDto patchProductDto = new PatchProductDto("Coca","Descripcion",2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.patch("/product/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Coca"));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void testChangeProductAvailability_Success() throws Exception {
        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);
        Long productId = producto.getId();

        // Crea el DTO para cambiar la disponibilidad
        PatchAvailability patchProductDto = new PatchAvailability(false);  // Cambia la disponibilidad a false

        // Realiza la solicitud PATCH
        mockMvc.perform(MockMvcRequestBuilders.patch("/product/changeAvailability/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isAvailable").value(false));  // Verifica que la disponibilidad sea false
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProductByCategory_Success() throws Exception {
        setproductos();

        String category = "APERITIVO";
        mockMvc.perform(get("/product/category/" + category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));// Verifica que haya 2 productos en esta categoría
    }





    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAvailableProducts_Success() throws Exception {
        setproductos();
        mockMvc.perform(get("/product/available")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    void setproductos(){
        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        producto.setIsAvailable(true);
        productRepository.save(producto);

        Product producto2 = new Product();
        producto2.setCategory(Category.APERITIVO);
        producto2.setNombre("Inka kola");
        producto2.setDescripcion("Muy rico");
        producto2.setPrice(2.0);
        producto2.setIsAvailable(false);
        productRepository.save(producto2);

        Product producto3 = new Product();
        producto3.setCategory(Category.BEDIDA);
        producto3.setNombre("Inka kola");
        producto3.setDescripcion("Muy rico");
        producto3.setPrice(2.0);
        producto3.setIsAvailable(true);
        productRepository.save(producto3);



    }










































}


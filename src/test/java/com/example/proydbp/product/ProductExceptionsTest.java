package com.example.proydbp.product;

import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.dto.PatchAvailability;
import com.example.proydbp.product.dto.PatchProductDto;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductExceptionsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetProductById_NotFound() throws Exception {
        productRepository.deleteAll();
        mockMvc.perform(get("/product/" + 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_BadRequestPrecio() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto("Coca","Descripcion",-2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_BadRequestNombre() throws Exception {
        String des = "asdfasdfahsdkjfajsdkj fakjlsdhfkjlasdkjfahsdkj faskjd falkjsdhfa kljdsh fkjashdfkjahsdkljfh askdjlfhaklsdh fakjlsdhfak";
        ProductRequestDto productRequestDto = new ProductRequestDto(des,"Descripcion",2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteProduct_NotFound() throws Exception {

        productRepository.deleteAll();

        mockMvc.perform(delete("/product/" + 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProduct_BadRequest() throws Exception {
        productRepository.deleteAll();
        Product producto = new Product();
        producto.setCategory(Category.APERITIVO);
        producto.setNombre("Inka kola");
        producto.setDescripcion("Muy rico");
        producto.setPrice(2.0);
        productRepository.save(producto);
        Long productId = producto.getId();

        PatchProductDto patchProductDto = new PatchProductDto("Coca","Descripcion",-2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.patch("/product/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchProductDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProduct_NotFound() throws Exception {
        productRepository.deleteAll();


        PatchProductDto patchProductDto = new PatchProductDto("Coca","Descripcion",2.0, Category.BEDIDA,true);

        mockMvc.perform(MockMvcRequestBuilders.patch("/product/" + 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchProductDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testChangeProductAvailability_NotFound() throws Exception {
        productRepository.deleteAll();
        PatchAvailability patchProductDto = new PatchAvailability(false);  // Cambia la disponibilidad a false
        mockMvc.perform(MockMvcRequestBuilders.patch("/product/changeAvailability/" + 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchProductDto)))
                .andExpect(status().isNotFound());
    }













































}

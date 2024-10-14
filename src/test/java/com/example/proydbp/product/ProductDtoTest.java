package com.example.proydbp.product;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.product.domain.Category;
import com.example.proydbp.product.dto.ProductRequestDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductDtoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_InvalidPrice() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto("Nombre","Descripcion",-2.0, Category.BEDIDA,true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_InvalidDescription() throws Exception {
        String desc= "poqewirqewqrwoeirqpoweirq´wierpqowierqpowierqpowie rpoasdklfjas kldfjaklsdjfakñlsjdfklajsd fkajsdkfjañsldkjfañ lskdfjañlksdjfaklsdjfaklsdjfaksjadfjasdkfjaksldjfaksjdfkajsdfklajsdkfjadsklf";
        ProductRequestDto productRequestDto = new ProductRequestDto("Nombre",desc,-2.0, Category.BEDIDA,true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateProduct_InvalidName() throws Exception {
        String desc= "poqewirqewqrwoeirqpoweirq´wierpqowierqpowierqpowie rpoasdklfjas kldfjaklsdjfakñlsjdfklajsd fkajsdkfjañsldkjfañ lskdfjañlksdjfaklsdjfaklsdjfaksjadfjasdkfjaksldjfaksjdfkajsdfklajsdkfjadsklf";
        ProductRequestDto productRequestDto = new ProductRequestDto(desc,"descripcion",-2.0, Category.BEDIDA,true);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/product/")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isBadRequest());
    }





























}




























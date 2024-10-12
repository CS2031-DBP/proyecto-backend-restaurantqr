package com.example.proydbp.orden;


import com.example.proydbp.orden.dto.OrderRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldFailValidationWhenPriceIsNull() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(null);
        orderRequestDto.setProductosIds(Collections.singletonList(1L));
        orderRequestDto.setDetails("Valid details");

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(orderRequestDto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenPriceIsNegative() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(BigDecimal.valueOf(-10.00));
        orderRequestDto.setProductosIds(Collections.singletonList(1L));
        orderRequestDto.setDetails("Valid details");

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(orderRequestDto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenProductosIdsAreEmpty() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(BigDecimal.valueOf(10.00));
        orderRequestDto.setProductosIds(Collections.emptyList());
        orderRequestDto.setDetails("Valid details");

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(orderRequestDto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenDetailsAreTooLong() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(BigDecimal.valueOf(10.00));
        orderRequestDto.setProductosIds(Collections.singletonList(1L));
        orderRequestDto.setDetails("This detail is way too long to be valid and exceeds the maximum limit of 250 characters. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(orderRequestDto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setPrice(BigDecimal.valueOf(10.00));
        orderRequestDto.setProductosIds(Collections.singletonList(1L));
        orderRequestDto.setDetails("Valid order details");

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(orderRequestDto);

        assertEquals(0, violations.size());
    }
}
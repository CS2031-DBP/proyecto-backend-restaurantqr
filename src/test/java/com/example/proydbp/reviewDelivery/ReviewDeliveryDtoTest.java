package com.example.proydbp.reviewDelivery;

import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
    public class ReviewDeliveryDtoTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Configuración del validador local para validar el DTO
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldPassValidationWithValidData() {
        // Crear un ReviewDeliveryRequestDto con datos válidos
        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "repartidor@example.com",
                5.0,
                "Buen servicio",
                1L
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que no haya errores de validación
        assertEquals(0, violations.size());
    }

    @Test
    void shouldFailValidationWhenRatingScoreIsNull() {
        // Crear un ReviewDeliveryRequestDto con ratingScore nulo
        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "repartidor@example.com",
                null, // Campo nulo
                "Buen servicio",
                1L
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que haya 1 violación de restricción relacionada con ratingScore
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenCommentExceedsMaxSize() {
        // Crear un ReviewDeliveryRequestDto con un comentario demasiado largo (más de 250 caracteres)
        String longComment = "A".repeat(251);

        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "repartidor@example.com",
                5.0,
                longComment,  // Comentario demasiado largo
                1L
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que haya una violación relacionada con el tamaño del comentario
        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailValidationWhenEmailRepartidorIsInvalid() {
        // Crear un ReviewDeliveryRequestDto con un email inválido
        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "invalid-email",  // Email inválido
                5.0,
                "Buen servicio",
                1L
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que haya 1 violación de restricción relacionada con el emailRepartidor
        assertEquals(1, violations.size());
    }

    @Test
    void shouldPassValidationWhenIdDeliveryIsNull() {
        // Crear un ReviewDeliveryRequestDto con idDelivery nulo
        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "repartidor@example.com",
                4.0,
                "Buen servicio",
                null  // idDelivery nulo
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que no haya violaciones (idDelivery puede ser nulo)
        assertEquals(0, violations.size());
    }

    @Test
    void shouldFailValidationWhenCommentIsEmpty() {
        // Crear un ReviewDeliveryRequestDto con un comentario vacío
        ReviewDeliveryRequestDto dto = new ReviewDeliveryRequestDto(
                "repartidor@example.com",
                4.0,
                "",  // Comentario vacío
                1L
        );

        // Validar el objeto DTO
        Set<ConstraintViolation<ReviewDeliveryRequestDto>> violations = validator.validate(dto);

        // Verificar que haya 1 violación de restricción relacionada con el comentario
        assertEquals(1, violations.size());
    }
}

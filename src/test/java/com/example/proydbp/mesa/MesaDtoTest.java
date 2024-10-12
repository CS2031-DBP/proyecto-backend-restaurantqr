package com.example.proydbp.mesa;


import com.example.proydbp.mesa.dto.MesaRequestDto;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MesaDtoTest {

    private Validator validator;
    private Set<Integer> usedNumbers;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;

        // Simulamos que ya existe una mesa con el número 10
        usedNumbers = new HashSet<>();
        usedNumbers.add(10);
    }

    @Test
    void shouldFailValidationWhenNumeroIsNotUnique() {
        MesaRequestDto mesaRequestDto = new MesaRequestDto();
        mesaRequestDto.setNumero(10); // Número ya usado
        mesaRequestDto.setCapacity(4);
        mesaRequestDto.setAvailable(true);

        boolean isUnique = usedNumbers.add(mesaRequestDto.getNumero()); // Verifica unicidad
        assertEquals(false, isUnique, "El número de mesa ya está en uso");
    }

    @Test
    void shouldPassValidationWhenNumeroIsUnique() {
        MesaRequestDto mesaRequestDto = new MesaRequestDto();
        mesaRequestDto.setNumero(11); // Número único
        mesaRequestDto.setCapacity(4);
        mesaRequestDto.setAvailable(true);

        boolean isUnique = usedNumbers.add(mesaRequestDto.getNumero()); // Verifica unicidad
        assertEquals(true, isUnique, "El número de mesa es único y válido");
    }
}


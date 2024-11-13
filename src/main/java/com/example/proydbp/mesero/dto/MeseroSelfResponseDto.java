package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MeseroSelfResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Float ratingScore;
}

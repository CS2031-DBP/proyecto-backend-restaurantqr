package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MeseroSelfResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Float ratingScore;
}

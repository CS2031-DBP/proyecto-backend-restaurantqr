package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MeseroSelfResponseDto {

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @Email
    private String email;

    @NotNull
    @Size(min = 9, max = 12)
    private String phoneNumber;

    @NotNull
    private Float ratingScore;
}

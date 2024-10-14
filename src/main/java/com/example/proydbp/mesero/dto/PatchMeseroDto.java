package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMeseroDto {

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
    @Size(min = 6)
    private String password;
}

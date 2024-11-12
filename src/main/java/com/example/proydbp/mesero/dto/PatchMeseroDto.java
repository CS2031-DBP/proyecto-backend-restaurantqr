package com.example.proydbp.mesero.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchMeseroDto {


    private String firstName;

    private String lastName;

    private String phone;

    private String password;
}

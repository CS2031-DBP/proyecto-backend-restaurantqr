package com.example.proydbp.client.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchClientDto {


    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private String email;
}

package com.example.proydbp.auth.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
}

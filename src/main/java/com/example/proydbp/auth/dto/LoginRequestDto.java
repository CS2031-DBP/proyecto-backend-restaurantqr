package com.example.proydbp.auth.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
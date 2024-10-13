package com.example.proydbp.client.dto;

import lombok.Data;

@Data
public class PatchClientDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
}

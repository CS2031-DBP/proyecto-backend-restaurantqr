package com.example.proydbp.client.dto;

import com.example.proydbp.client.domain.Rango;
import lombok.Data;

import java.util.List;

@Data
public class ClientResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Rango rango;

    private Integer loyaltyPoints;
}

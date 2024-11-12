package com.example.proydbp.client.dto;

import com.example.proydbp.client.domain.Rango;
import lombok.Data;

@Data
public class ClientSelfResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Rango rango;

    private Integer loyaltyPoints;
}

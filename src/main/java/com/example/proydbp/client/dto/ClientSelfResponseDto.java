package com.example.proydbp.client.dto;

import com.example.proydbp.client.domain.Rango;
import lombok.Data;

@Data
public class ClientSelfResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Rango rango;

    private Integer loyaltyPoints;
}

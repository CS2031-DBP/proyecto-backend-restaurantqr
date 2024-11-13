package com.example.proydbp.repartidor.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PatchRepartidorDto {

    private String firstName;

    private String lastName;

    private String password;

    private String phone;
}

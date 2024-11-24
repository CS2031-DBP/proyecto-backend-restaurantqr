package com.example.proydbp.mesa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MesaSelfReponseDto {

    private String qr;

    private Long id;

    private Integer capacity;

    private boolean isAvailable;

}

package com.example.proydbp.delivery.dto;


import lombok.Data;

import java.util.List;

@Data
public class DeliveryRequestDto {

    private String direccion;
    private String descripcion;
    private List<Long> idProducts;

}

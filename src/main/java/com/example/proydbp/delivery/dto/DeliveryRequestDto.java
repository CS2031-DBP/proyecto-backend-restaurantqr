package com.example.proydbp.delivery.dto;



import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class DeliveryRequestDto {

    private String direccion;
    private Double costoDelivery;

    private LocalDate fecha;
    private LocalTime hora;
    private Double precio;

}

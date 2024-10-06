package com.example.proydbp.table.dto;

import lombok.Data;

@Data
public class TableDto {

    private String qrCode; // Código QR asociado a la mesa
    private String location; // Ubicación de la mesa
    private Integer capacity;
    private boolean isAvailable;
}

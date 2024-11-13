package com.example.proydbp.repartidor.dto;

import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RepartidorResponseDto {

    private Long id;

    private String firstName;


    private String lastName;

    private String email;

    private String phone;

    private List<DeliveryResponseDto> deliveries; // Suponiendo que tienes un DTO para Delivery

    private List<ReviewDeliveryResponseDto> reviewDeliveries; // Suponiendo que tienes un DTO para ReviewDelivery

    private Double ratingScore;

}

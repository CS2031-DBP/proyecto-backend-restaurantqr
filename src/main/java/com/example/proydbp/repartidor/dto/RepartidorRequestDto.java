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
public class RepartidorRequestDto {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

}

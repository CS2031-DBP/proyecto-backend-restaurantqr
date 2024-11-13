package com.example.proydbp.client.dto;

import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import lombok.Data;

import java.util.List;

@Data
public class ClientResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private List<PedidoLocalResponseDto> pedidosLocales ;

    private List<DeliveryResponseDto> deliveries;

    private List<ReservationResponseDto> reservations;

    private List<ReviewMesero> reviewMeseros;

    private List<ReviewDeliveryResponseDto> reviewDeliveries;

    private Rango rango;

    private Integer loyaltyPoints;
}

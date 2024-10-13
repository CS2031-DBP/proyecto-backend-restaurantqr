package com.example.proydbp.client.dto;

import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class ClientResponseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<ReviewDeliveryResponseDto> reviewDeliveries;

    private List<ReviewMeseroResponseDto> reviewMeseros;

    private List<PedidoLocalResponseDto> pedidoLocals;

    private List<DeliveryResponseDto> deliveries;

    private List<ReservationResponseDto> reservations;
}

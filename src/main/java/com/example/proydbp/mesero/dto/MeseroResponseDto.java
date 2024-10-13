package com.example.proydbp.mesero.dto;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeseroResponseDto {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String phoneNumber;

    private List<PedidoLocalResponseDto> pedidosLocales;

    private List<ReviewMeseroResponseDto> reviewsMesero;

    @DecimalMax("5")
    @DecimalMin("0")
    private Double ratingScore;
}

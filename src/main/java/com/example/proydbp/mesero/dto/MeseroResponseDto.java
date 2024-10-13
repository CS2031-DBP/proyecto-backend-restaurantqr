package com.example.proydbp.mesero.dto;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeseroResponseDto {

    @NotNull
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    private List<PedidoLocal> pedidosLocales;

    private List<ReviewMesero> reviewsMesero;

    @DecimalMax("5")
    @DecimalMin("0")
    private Double ratingScore;
}

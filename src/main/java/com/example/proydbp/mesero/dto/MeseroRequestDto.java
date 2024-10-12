package com.example.proydbp.mesero.dto;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeseroRequestDto {

    @Nullable
    private List<PedidoLocal> pedidosLocales;

    @Nullable
    private List<ReviewMesero> reviewsMesero;

    @DecimalMax("5")
    @DecimalMin("0")
    private Double ratingScore;
}

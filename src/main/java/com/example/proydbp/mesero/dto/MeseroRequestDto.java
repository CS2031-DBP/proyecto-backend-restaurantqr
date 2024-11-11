package com.example.proydbp.mesero.dto;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeseroRequestDto {


    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;


}

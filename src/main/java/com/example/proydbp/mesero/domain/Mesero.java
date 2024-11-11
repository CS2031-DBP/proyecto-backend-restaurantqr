package com.example.proydbp.mesero.domain;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mesero extends User {

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewMesero> reviewMesero = new ArrayList<>();

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoLocal> pedidosLocales = new ArrayList<>();

    private Double ratingScore;

}

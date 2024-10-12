package com.example.proydbp.mesero.domain;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mesero extends User {

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL)
    private List<PedidoLocal> pedidosLocales;

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL)
    private List<ReviewMesero> reviewsMesero;

    private Double ratingScore;
}

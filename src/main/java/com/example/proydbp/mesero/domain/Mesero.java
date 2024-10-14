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

    // Esto debe ser una colección mutable
    @Getter
    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewMesero> reviewMesero = new ArrayList<>(); // Asegúrate de que sea una ArrayList

    // Esto debe ser una colección mutable
    @Getter
    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoLocal> pedidosLocales = new ArrayList<>(); // Asegúrate de que sea una ArrayList


    private Double ratingScore;

    public Mesero(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.setPassword(password);
    }

}

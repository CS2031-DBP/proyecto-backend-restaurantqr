package com.example.proydbp.pedido_local.infrastructure;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoLocalInterface extends JpaRepository<PedidoLocal, Long> {
}

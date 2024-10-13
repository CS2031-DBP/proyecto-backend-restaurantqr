package com.example.proydbp.pedido_local.infrastructure;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoLocalRepository extends JpaRepository<PedidoLocal, Long> {
    List<PedidoLocal> findByStatus(String estado);
}

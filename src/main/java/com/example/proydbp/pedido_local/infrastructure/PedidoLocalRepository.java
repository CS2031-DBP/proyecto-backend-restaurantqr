package com.example.proydbp.pedido_local.infrastructure;

import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoLocalRepository extends JpaRepository<PedidoLocal, Long> {
    List<PedidoLocal> findByStatus(StatusPedidoLocal estado);

    List<PedidoLocal> findByStatusIn(List<StatusPedidoLocal> statuses);

    Page<PedidoLocal> findByMeseroAndStatusNot(Mesero mesero, StatusPedidoLocal statusPedidoLocal, Pageable pageable);

    Page<PedidoLocal> findByMesero(Mesero mesero, Pageable pageable);
}
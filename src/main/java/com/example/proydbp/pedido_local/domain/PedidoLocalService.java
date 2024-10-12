package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.domain.MeseroService;
import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoLocalService {

    final private PedidoLocalRepository pedidoLocalRepository;
    final private MeseroService meseroService;

    @Autowired
    public PedidoLocalService (PedidoLocalRepository pedidoLocalRepository, MeseroService meseroService) {
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.meseroService = meseroService;
    }

    public PedidoLocalResponseDto findPedidoLocalById(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal not found"));
        return mapToDto(pedidoLocal);
    }

    public List<PedidoLocalResponseDto> findAllPedidoLocals() {
        List<PedidoLocal> pedidos = pedidoLocalRepository.findAll();
        return pedidos.stream().map(this::mapToDto).toList();
    }

    public PedidoLocalResponseDto createPedidoLocal(PedidoLocalRequestDto dto) {
        Mesero mesero = meseroService.findMeseroEntityById(dto.getMeseroId());
        PedidoLocal pedidoLocal = new PedidoLocal();
        pedidoLocal.setOrdenes(dto.getOrdenes());
        pedidoLocal.setMesero(mesero);
        pedidoLocal.setFecha(dto.getFecha());
        pedidoLocal.setHora(dto.getHora());
        pedidoLocal.setEstado(dto.getEstado());
        pedidoLocal.setPrecio(dto.getPrecio());
        pedidoLocal.setTipoPago(dto.getTipoPago());
        pedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Evento de creación
        // publishEvent(new PedidoLocalCreadoEvent(pedidoLocal));

        return mapToDto(pedidoLocal);
    }

    public void deletePedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal not found"));
        pedidoLocalRepository.delete(pedidoLocal);

        // Evento de eliminación
        // publishEvent(new PedidoLocalEliminadoEvent(pedidoLocal));
    }

    public PedidoLocalResponseDto updatePedidoLocal(Long id, PatchPedidoLocalDto dto) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal not found"));

        if (dto.getOrdenes() != null) {
            pedidoLocal.setOrdenes(dto.getOrdenes());
        }
        if (dto.getMeseroId() != null) {
            Mesero mesero = meseroService.findMeseroEntityById(dto.getMeseroId());
            pedidoLocal.setMesero(mesero);
        }
        if (dto.getEstado() != null) {
            pedidoLocal.setEstado(dto.getEstado());
        }

        pedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Evento de actualización
        // publishEvent(new PedidoLocalActualizadoEvent(pedidoLocal));

        return mapToDto(pedidoLocal);
    }

    public void cocinandoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal not found"));
        pedidoLocal.setEstado("EN_PREPARACION");
        pedidoLocalRepository.save(pedidoLocal);

        // Evento de cambio de estado
        // publishEvent(new EstadoPedidoLocalPreparandoEvent(pedidoLocal));
    }

    public void listoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal not found"));
        pedidoLocal.setEstado("LISTO");
        pedidoLocalRepository.save(pedidoLocal);

        // Evento de cambio de estado
        // publishEvent(new EstadoPedidoLocalListoEvent(pedidoLocal));
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesRecibidos() {
        List<PedidoLocal> pedidosRecibidos = pedidoLocalRepository.findByEstado("RECIBIDO");
        return pedidosRecibidos.stream().map(this::mapToDto).toList();
    }

    // Mapeo de entidad a DTO
    private PedidoLocalResponseDto mapToDto(PedidoLocal pedidoLocal) {
        // Mapeo manual o usando un mapper tipo MapStruct
        return new PedidoLocalResponseDto(
                pedidoLocal.getId(),
                pedidoLocal.getOrdenes(),
                pedidoLocal.getMesero(),
                pedidoLocal.getFecha(),
                pedidoLocal.getHora(),
                pedidoLocal.getEstado(),
                pedidoLocal.getPrecio(),
                pedidoLocal.getTipoPago()
        );
    }
}

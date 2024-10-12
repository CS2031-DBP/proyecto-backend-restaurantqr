package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.events.email_event.*;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.orden.domain.Order;
import com.example.proydbp.orden.dto.OrderResponseDto;
import com.example.proydbp.orden.infrastructure.OrderRepository;
import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoLocalService {

    final private PedidoLocalRepository pedidoLocalRepository;
    final private ApplicationEventPublisher eventPublisher;
    final private OrderRepository orderRepository;
    final private MeseroRepository meseroRepository;

    @Autowired
    public PedidoLocalService(PedidoLocalRepository pedidoLocalRepository
            , ApplicationEventPublisher eventPublisher, OrderRepository orderRepository,
                              MeseroRepository meseroRepository) {
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.eventPublisher = eventPublisher;
        this.orderRepository = orderRepository;
        this.meseroRepository = meseroRepository;
    }

    public PedidoLocalResponseDto findPedidoLocalById(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));
        return mapToResponseDto(pedidoLocal);
    }

    public List<PedidoLocalResponseDto> findAllPedidoLocals() {
        return pedidoLocalRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public PedidoLocalResponseDto createPedidoLocal(PedidoLocalRequestDto dto) {
        PedidoLocal pedidoLocal = mapToEntity(dto);
        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Obtener el correo del mesero asociado al pedido
        String recipientEmail = savedPedidoLocal.getMesero().getEmail();

        eventPublisher.publishEvent(new PedidoLocalCreatedEvent(savedPedidoLocal, recipientEmail));

        return mapToResponseDto(savedPedidoLocal);
    }


    public void deletePedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        // Publicar el evento antes de eliminar el pedido
        String recipientEmail = "fernando.munoz.p@utec.edu.pe";
        eventPublisher.publishEvent(new PedidoLocalDeletedEvent(id, pedidoLocal, recipientEmail));

        pedidoLocalRepository.deleteById(id);
    }


    public PedidoLocalResponseDto updatePedidoLocal(Long id, PatchPedidoLocalDto dto) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));
        mapPatchDtoToEntity(dto, pedidoLocal);
        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        eventPublisher.publishEvent(new PedidoLocalUpdatedEvent(updatedPedidoLocal, recipientEmail));
        return mapToResponseDto(updatedPedidoLocal);
    }

    public PedidoLocalResponseDto cocinandoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        pedidoLocal.setEstado("EN_PREPARACION");

        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        // Publicar el evento
        eventPublisher.publishEvent(new EstadoPedidoLocalPreparandoEvent(updatedPedidoLocal, recipientEmail));

        return mapToResponseDto(updatedPedidoLocal);
    }


    public PedidoLocalResponseDto listoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        pedidoLocal.setEstado("ENTREGADO");

        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        // Publicar el evento
        eventPublisher.publishEvent(new EstadoPedidoLocalListoEvent(updatedPedidoLocal, recipientEmail));

        return mapToResponseDto(updatedPedidoLocal);
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesRecibidos() {
        List<PedidoLocal> pedidosLocales = pedidoLocalRepository.findByStatus("RECIBIDO");

        return pedidosLocales.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    private PedidoLocalResponseDto mapToResponseDto(PedidoLocal pedidoLocal) {
        PedidoLocalResponseDto responseDto = new PedidoLocalResponseDto();
        responseDto.setId(pedidoLocal.getId());

        // Mapeo de cada orden a su correspondiente OrderResponseDto
        List<OrderResponseDto> ordersResponse = pedidoLocal.getOrdenes().stream()
                .map(order -> new OrderResponseDto(order.getId(), order.getPrice(), order.getProducts(), order.getDetails()))
                .collect(Collectors.toList());
        responseDto.setOrdenes(ordersResponse);

        // Mapeo del mesero usando MeseroResponseDto
        Mesero mesero = pedidoLocal.getMesero();
        if (mesero != null) {
            MeseroResponseDto meseroDto = new MeseroResponseDto(
                    mesero.getId(),
                    mesero.getFirstName(),
                    mesero.getLastName(),
                    mesero.getPedidosLocales(),
                    mesero.getReviewsMesero(),
                    mesero.getRatingScore()
            );
            responseDto.setMesero(meseroDto);
        }

        responseDto.setFecha(pedidoLocal.getFecha());
        responseDto.setHora(pedidoLocal.getHora());
        responseDto.setEstado(pedidoLocal.getEstado());
        responseDto.setPrecio(pedidoLocal.getPrecio());
        responseDto.setTipoPago(pedidoLocal.getTipoPago());

        return responseDto;
    }

    private PedidoLocal mapToEntity(PedidoLocalRequestDto dto) {
        PedidoLocal pedidoLocal = new PedidoLocal();

        // Mapeo de las órdenes
        List<Order> orders = dto.getOrdenesIds().stream()
                .map(orderId -> orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con ID: " + orderId)))
                .collect(Collectors.toList());
        pedidoLocal.setOrdenes(orders);

        // Mapeo del mesero desde el repositorio
        Mesero mesero = meseroRepository.findById(dto.getMeseroId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesero no encontrado"));
        pedidoLocal.setMesero(mesero);

        pedidoLocal.setFecha(dto.getFecha());
        pedidoLocal.setHora(dto.getHora());
        pedidoLocal.setEstado(dto.getEstado());
        pedidoLocal.setPrecio(dto.getPrecio());
        pedidoLocal.setTipoPago(dto.getTipoPago());

        return pedidoLocal;
    }

    private void mapPatchDtoToEntity(PatchPedidoLocalDto dto, PedidoLocal pedidoLocal) {
        if (dto.getEstado() != null) {
            pedidoLocal.setEstado(dto.getEstado());
        }
        if (dto.getPrecio() != null) {
            pedidoLocal.setPrecio(dto.getPrecio());
        }
        if (dto.getTipoPago() != null) {
            pedidoLocal.setTipoPago(dto.getTipoPago());
        }
    }
}

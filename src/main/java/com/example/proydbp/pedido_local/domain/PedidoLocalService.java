package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.events.email_event.*;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesero.domain.MeseroService;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoLocalService {

    private final PedidoLocalRepository pedidoLocalRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;
    private final MeseroService meseroService;

    @Autowired
    public PedidoLocalService(PedidoLocalRepository pedidoLocalRepository,
                              ApplicationEventPublisher eventPublisher,
                              ModelMapper modelMapper,
                              MeseroService meseroService) {
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
        this.meseroService = meseroService;
    }

    public PedidoLocalResponseDto findPedidoLocalById(Long id) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);
        return modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findAllPedidoLocals() {
        return pedidoLocalRepository.findAll().stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public PedidoLocalResponseDto createPedidoLocal(PedidoLocalRequestDto dto) {
        PedidoLocal pedidoLocal = modelMapper.map(dto, PedidoLocal.class);
        pedidoLocal.setMesero(meseroService.asignarMesero());
        pedidoLocal.setStatus(StatusPedidoLocal.RECIBIDO);
        pedidoLocal.setFecha(LocalDate.now());
        pedidoLocal.setHora(LocalTime.now());

        // Guardar el pedido local
        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        savedPedidoLocal.setPrecio(calcularPrecioTotal(savedPedidoLocal.getId()));

        // Publicar el evento
        String recipientEmail = savedPedidoLocal.getMesero().getEmail();
        eventPublisher.publishEvent(new PedidoLocalCreatedEvent(savedPedidoLocal, recipientEmail));

        return modelMapper.map(savedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public void deletePedidoLocal(Long id) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);
        String recipientEmail = "fernando.munoz.p@utec.edu.pe"; // Se podría cambiar a la lógica correspondiente

        // Publicar el evento antes de eliminar
        eventPublisher.publishEvent(new PedidoLocalDeletedEvent(id, pedidoLocal, recipientEmail));
        pedidoLocalRepository.deleteById(id);
    }

    public PedidoLocalResponseDto updatePedidoLocal(Long id, PatchPedidoLocalDto dto) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);

        // Mapear solo los campos que se actualizan desde el DTO
        modelMapper.map(dto, pedidoLocal);
        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        updatedPedidoLocal.setPrecio(calcularPrecioTotal(updatedPedidoLocal.getId()));

        // Publicar el evento
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();
        eventPublisher.publishEvent(new PedidoLocalUpdatedEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public PedidoLocalResponseDto cocinandoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);
        pedidoLocal.setStatus(StatusPedidoLocal.EN_PREPARACION);

        // Guardar y publicar el evento
        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();
        eventPublisher.publishEvent(new EstadoPedidoLocalPreparandoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public PedidoLocalResponseDto listoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);
        pedidoLocal.setStatus(StatusPedidoLocal.LISTO);

        // Guardar y publicar el evento
        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();
        eventPublisher.publishEvent(new EstadoPedidoLocalListoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesActuales() {
        List<PedidoLocal> pedidosRecibidos = pedidoLocalRepository.findByStatus(StatusPedidoLocal.valueOf(String.valueOf(StatusPedidoLocal.RECIBIDO)));
        List<PedidoLocal> pedidosEnPreparacion = pedidoLocalRepository.findByStatus(StatusPedidoLocal.valueOf(String.valueOf(StatusPedidoLocal.EN_PREPARACION)));

        List<PedidoLocal> todosPedidos = new ArrayList<>();
        todosPedidos.addAll(pedidosRecibidos);
        todosPedidos.addAll(pedidosEnPreparacion);

        return todosPedidos.stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }


    public double calcularPrecioTotal(Long idPedidoLocal) {
        PedidoLocal pedidoLocal = getPedidoLocalById(idPedidoLocal);
        List<Order> orders = pedidoLocal.getOrders();

        // Calcular el precio total sumando el precio de cada orden
        return orders.stream()
                .mapToDouble(Order::getPrice)
                .sum();
    }

    public PedidoLocalResponseDto entregadoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = getPedidoLocalById(id);
        pedidoLocal.setStatus(StatusPedidoLocal.ENTREGADO);

        // Guardar y publicar el evento
        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();
        eventPublisher.publishEvent(new EstadoPedidoLocalListoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    private PedidoLocal getPedidoLocalById(Long id) {
        return pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));
    }
}

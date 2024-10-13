package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoLocalService {

    final private PedidoLocalRepository pedidoLocalRepository;
    final private ApplicationEventPublisher eventPublisher;
    final private ModelMapper modelMapper;
    final private MeseroService meseroService;
    private final ClientRepository clientRepository;

    @Autowired
    public PedidoLocalService(PedidoLocalRepository pedidoLocalRepository
            , ApplicationEventPublisher eventPublisher, ModelMapper modelMapper, MeseroService meseroService, ClientRepository clientRepository) {
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
        this.meseroService = meseroService;
        this.clientRepository = clientRepository;
    }

    public PedidoLocalResponseDto findPedidoLocalById(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        return modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findAllPedidoLocals() {
        return pedidoLocalRepository.findAll().stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public PedidoLocalResponseDto createPedidoLocal(PedidoLocalRequestDto dto) {
        PedidoLocal pedidoLocal = modelMapper.map(dto, PedidoLocal.class); // Mapeo directo desde DTO a entidad
        pedidoLocal.setMesero(meseroService.asignarMesero());
        pedidoLocal.setStatus(StatusPedidoLocal.RECIBIDO);
        pedidoLocal.setFecha(LocalDate.now());
        pedidoLocal.setHora(LocalTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        pedidoLocal.setClient(client);
        PedidoLocal pedidoLocal2 = pedidoLocalRepository.save(pedidoLocal);

        pedidoLocal2.setPrecio(calcularPrecioTotal(pedidoLocal.getId()));

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal2);

        String recipientEmail = savedPedidoLocal.getMesero().getEmail();

        eventPublisher.publishEvent(new PedidoLocalCreatedEvent(savedPedidoLocal, recipientEmail));

        return modelMapper.map(savedPedidoLocal, PedidoLocalResponseDto.class);
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

        // Mapear solo los campos que se actualizan desde el DTO
        modelMapper.map(dto, pedidoLocal);

        PedidoLocal pedidoLocal2 = pedidoLocalRepository.save(pedidoLocal);

        pedidoLocal2.setPrecio(calcularPrecioTotal(pedidoLocal.getId()));

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal2);

        String recipientEmail = savedPedidoLocal.getMesero().getEmail();

        eventPublisher.publishEvent(new PedidoLocalUpdatedEvent(savedPedidoLocal, recipientEmail));
        return modelMapper.map(savedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public PedidoLocalResponseDto cocinandoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        pedidoLocal.setStatus(StatusPedidoLocal.EN_PREPARACION);

        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        // Publicar el evento
        eventPublisher.publishEvent(new EstadoPedidoLocalPreparandoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public PedidoLocalResponseDto listoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        pedidoLocal.setStatus(StatusPedidoLocal.LISTO);

        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        // Publicar el evento
        eventPublisher.publishEvent(new EstadoPedidoLocalListoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesActuales() {
        List<PedidoLocal> pedidosLocales = pedidoLocalRepository.findByStatusIn(List.of(StatusPedidoLocal.RECIBIDO, StatusPedidoLocal.EN_PREPARACION));

        // Mapear los pedidos locales a PedidoLocalResponseDto utilizando ModelMapper
        return pedidosLocales.stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }


    // adicional

    public double calcularPrecioTotal(Long idPedidoLocal) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(idPedidoLocal)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        // Obtener todas las órdenes asociadas al pedido local
        List<Order> orders = pedidoLocal.getOrders();

        // Calcular el precio total sumando el precio de cada orden
        double totalPrecio = orders.stream()
                .mapToDouble(Order::getPrice) // Obtener el precio de cada orden como double
                .sum(); // Sumar los precios

        return totalPrecio; // Retornar el precio total
    }


    public PedidoLocalResponseDto entregadoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal no encontrado"));

        pedidoLocal.setStatus(StatusPedidoLocal.ENTREGADO);

        PedidoLocal updatedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);
        String recipientEmail = updatedPedidoLocal.getMesero().getEmail();

        // Publicar el evento
        eventPublisher.publishEvent(new EstadoPedidoLocalListoEvent(updatedPedidoLocal, recipientEmail));

        return modelMapper.map(updatedPedidoLocal, PedidoLocalResponseDto.class);
    }

}

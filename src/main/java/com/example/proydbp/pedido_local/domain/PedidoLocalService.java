package com.example.proydbp.pedido_local.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.events.email_event.PedidoLocalCrearEvent;
import com.example.proydbp.events.email_event.PedidoLocalCrearMeseroEvent;
import com.example.proydbp.events.email_event.PedidoLocalEstadoChangeEvent;
import com.example.proydbp.events.email_event.PedidoLocalUpdateEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.mesero.domain.MeseroService;

import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PedidoLocalService {

    final private PedidoLocalRepository pedidoLocalRepository;
    final private ApplicationEventPublisher eventPublisher;
    final private ModelMapper modelMapper;
    final private MeseroService meseroService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final AuthorizationUtils authorizationUtils;
    private final MesaRepository mesaRepository;

    @Autowired
    public PedidoLocalService(PedidoLocalRepository pedidoLocalRepository, ApplicationEventPublisher eventPublisher,
                              MesaRepository mesaRepository,AuthorizationUtils authorizationUtils, ProductRepository productRepository,
                              ModelMapper modelMapper, @Lazy MeseroService meseroService, ClientRepository clientRepository) {
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.eventPublisher = eventPublisher;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.meseroService = meseroService;
        this.mesaRepository = mesaRepository;
        this.clientRepository = clientRepository;
        this.authorizationUtils = authorizationUtils;
    }

    public PedidoLocalResponseDto findPedidoLocalById(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con "+ id + " no encontrado"));
        return convertirADto(pedidoLocal);
    }

    public List<PedidoLocalResponseDto> findAllPedidoLocals() {
        List<PedidoLocalResponseDto> Pedidos = new ArrayList<>();

        for (PedidoLocal pedidoLocal : pedidoLocalRepository.findAll()) {

            PedidoLocalResponseDto responseDto =convertirADto(pedidoLocal);

            Pedidos.add(responseDto);
        }
        return Pedidos;
    }

    public PedidoLocalResponseDto createPedidoLocal(PedidoLocalRequestDto dto) {

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con " + username + " no encontrado"));

        Mesa mesa = mesaRepository.findById(dto.getMesaId())
                .orElseThrow(() -> new UsernameNotFoundException("Mesa con " + dto.getMesaId() + " no encontrada"));
        mesa.setAvailable(false);
        mesaRepository.save(mesa);

        List<String> nombresProductosFueraDeRango = new ArrayList<>();

        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresentOrElse(product -> {
                if (client.getRango().ordinal() < product.getRango().ordinal()) {
                    nombresProductosFueraDeRango.add(product.getNombre());
                }
            }, () -> {
                throw new ResourceNotFoundException("Producto " + id + " no encontrado");
            });
        }

        if (!nombresProductosFueraDeRango.isEmpty()) {
            throw new IllegalArgumentException("Los siguientes productos están fuera del rango permitido: " + String.join(", ", nombresProductosFueraDeRango));
        }

        PedidoLocal pedidoLocal = new PedidoLocal();
        pedidoLocal.setMesa(mesa);
        pedidoLocal.setClient(client);
        pedidoLocal.setTipoPago(dto.getTipoPago());
        pedidoLocal.setStatus(StatusPedidoLocal.RECIBIDO);
        pedidoLocal.setFecha(ZonedDateTime.now());
        pedidoLocal.setDescripcion(dto.getDescripcion());
        pedidoLocal.setIdProducts(dto.getIdProducts());
        pedidoLocal.setPrecio(0.0);
        pedidoLocal.setMesero(meseroService.asignarMesero());

        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresent(product -> {
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
                pedidoLocal.setPrecio(pedidoLocal.getPrecio() + product.getPrice());
            });
        }

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Publicar eventos para notificar al cliente y al mesero
        eventPublisher.publishEvent(new PedidoLocalCrearEvent(savedPedidoLocal, client.getEmail()));

        if (savedPedidoLocal.getMesero() != null) {
            eventPublisher.publishEvent(new PedidoLocalCrearMeseroEvent(savedPedidoLocal, savedPedidoLocal.getMesero().getEmail()));
        }
        return convertirADto(savedPedidoLocal);
    }

    public void deletePedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con "+ id + " no encontrado"));

        pedidoLocalRepository.deleteById(id);
    }

    public PedidoLocalResponseDto updatePedidoLocal(Long id, PatchPedidoLocalDto dto) {
        // Buscar el pedido local
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con " + id + " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, pedidoLocal.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        pedidoLocal.setDescripcion(dto.getDescripcion());
        pedidoLocal.setTipoPago(dto.getTipoPago());

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        eventPublisher.publishEvent(new PedidoLocalUpdateEvent(savedPedidoLocal, pedidoLocal.getClient().getEmail()));

        return convertirADto(savedPedidoLocal);
    }

    public PedidoLocalResponseDto cocinandoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con " + id + " no encontrado"));

        pedidoLocal.setStatus(StatusPedidoLocal.EN_PREPARACION);

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Publicar el evento
        eventPublisher.publishEvent(new PedidoLocalEstadoChangeEvent(savedPedidoLocal, pedidoLocal.getClient().getEmail()));

        return convertirADto(savedPedidoLocal);
    }


    public PedidoLocalResponseDto listoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con " + id + " no encontrado"));

        pedidoLocal.setStatus(StatusPedidoLocal.LISTO);

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Publicar el evento
        eventPublisher.publishEvent(new PedidoLocalEstadoChangeEvent(savedPedidoLocal, pedidoLocal.getClient().getEmail()));

        return convertirADto(savedPedidoLocal);
    }


    public List<PedidoLocalResponseDto> findPedidosLocalesActuales() {

        List<String> estados = List.of(StatusPedidoLocal.RECIBIDO.toString(), StatusPedidoLocal.EN_PREPARACION.toString());
        List<PedidoLocal> pedidosLocalesListos = pedidoLocalRepository.findByStatus(StatusPedidoLocal.RECIBIDO);
        List<PedidoLocal> pedidosLocalesPreparados = pedidoLocalRepository.findByStatus(StatusPedidoLocal.EN_PREPARACION);

        List<PedidoLocal> todosLosPedidosLocales = new ArrayList<>(pedidosLocalesListos);
        todosLosPedidosLocales.addAll(pedidosLocalesPreparados);

        // Mapear los pedidos locales a PedidoLocalResponseDto utilizando ModelMapper
        return todosLosPedidosLocales.stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public PedidoLocalResponseDto entregadoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con " + id + " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, pedidoLocal.getMesero().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo " + username + " no tiene permitido acceder a este recurso");
        }

        pedidoLocal.setStatus(StatusPedidoLocal.ENTREGADO);

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Publicar el evento
        eventPublisher.publishEvent(new PedidoLocalEstadoChangeEvent(savedPedidoLocal, pedidoLocal.getClient().getEmail()));

        return convertirADto(savedPedidoLocal);
    }


    public PedidoLocalResponseDto convertirADto(PedidoLocal pedidoLocal){
        PedidoLocalResponseDto responseDto = modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class);

        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id1 : pedidoLocal.getIdProducts()) {
            productRepository.findById(id1).ifPresent(product -> {
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
               // pedidoLocal.setPrecio(pedidoLocal.getPrecio()+ product.getPrice());
            });
        }
        responseDto.setProducts(productos);
        return  responseDto;
    }

    public PedidoLocalResponseDto canceladoPedidoLocal(Long id) {
        PedidoLocal pedidoLocal = pedidoLocalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PedidoLocal con " + id + " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, pedidoLocal.getMesero().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo " + username + " no tiene permitido acceder a este recurso");
        }

        pedidoLocal.setStatus(StatusPedidoLocal.CANCELADO);

        PedidoLocal savedPedidoLocal = pedidoLocalRepository.save(pedidoLocal);

        // Publicar el evento
        eventPublisher.publishEvent(new PedidoLocalEstadoChangeEvent(savedPedidoLocal, pedidoLocal.getClient().getEmail()));

        return convertirADto(savedPedidoLocal);
    }

}

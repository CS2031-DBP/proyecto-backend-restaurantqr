package com.example.proydbp.delivery.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.dto.PatchDeliveryDto;

import com.example.proydbp.events.email_event.DeliveryCrearEvent;
import com.example.proydbp.events.email_event.DeliveryCrearRepartidorEvent;
import com.example.proydbp.events.email_event.DeliveryEstadoChangeEvent;
import com.example.proydbp.events.email_event.DeliveryUpdateEvent;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.product.domain.Product;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.example.proydbp.repartidor.domain.RepartidorService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    final private DeliveryRepository deliveryRepository;
    final private ApplicationEventPublisher eventPublisher;
    final private ModelMapper modelMapper;
    private final RepartidorService repartidorService;
    private final AuthorizationUtils authorizationUtils;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, AuthorizationUtils authorizationUtils,
                           ApplicationEventPublisher eventPublisher, ModelMapper modelMapper,
                           @Lazy RepartidorService repartidorService, ClientRepository clientRepository,
                           ProductRepository productRepository) {
        this.deliveryRepository = deliveryRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
        this.repartidorService = repartidorService;
        this.authorizationUtils = authorizationUtils;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    public DeliveryResponseDto findDeliveryById(Long id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+ id + " no encontrado"));

        return convertirADto(delivery);
    }

    public Page<DeliveryResponseDto> findAllDeliveries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);

        List<DeliveryResponseDto> deliveriesResponse = deliveries.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return new PageImpl<>(deliveriesResponse, pageable, deliveries.getTotalElements());
    }


    public DeliveryResponseDto createDelivery(DeliveryRequestDto dto) {

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client cliente = clientRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con nombre de usuario " + username + " no encontrado"));

        List<String> nombresProductosFueraDeRango = new ArrayList<>();

        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresentOrElse(product -> {
                if (cliente.getRango().ordinal() < product.getRango().ordinal()) {
                    nombresProductosFueraDeRango.add(product.getNombre());
                }
            }, () -> {
                throw new ResourceNotFoundException("Producto " + id + " no encontrado");
            });
        }

        Delivery delivery = new Delivery();
        // Seteo de otros campos en el objeto Delivery
        delivery.setDireccion(dto.getDireccion());
        delivery.setDescripcion(dto.getDescripcion());
        delivery.setClient(cliente);  // Asociar el cliente al delivery
        delivery.setRepartidor(repartidorService.asignarRepartidor()); // Asignación del repartidor
        delivery.setStatus(StatusDelivery.RECIBIDO); // Estado inicial
        delivery.setFecha(ZonedDateTime.now()); // Fecha actual de creación
        delivery.setCostoDelivery(5.0); // Costo base de delivery (ajustar según lógica)
        delivery.setIdProducts(dto.getIdProducts());
        delivery.setPrecio(0.0); // Inicializar el precio

        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresentOrElse(product -> {
                System.out.println("Producto encontrado: " + product);
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
                delivery.setPrecio(delivery.getPrecio() + product.getPrice());
            }, () -> {
                System.out.println("Producto con ID " + id + " no encontrado");
            });

        }
        Delivery savedDelivery = deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryCrearEvent(savedDelivery, cliente.getEmail()));

        if (delivery.getRepartidor() != null) {
            eventPublisher.publishEvent(new DeliveryCrearRepartidorEvent(savedDelivery, delivery.getRepartidor().getEmail()));
        }
        return modelMapper.map(savedDelivery, DeliveryResponseDto.class);
    }

    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));
        deliveryRepository.deleteById(id);
    }

    public DeliveryResponseDto updateDelivery(Long id, PatchDeliveryDto dto) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con " + id + " no encontrado"));

        // Verificar que el usuario actual es el propietario del delivery
        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        delivery.setDescripcion(dto.getDescripcion());
        delivery.setDireccion(dto.getDireccion());

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryUpdateEvent(updatedDelivery, updatedDelivery.getRepartidor().getEmail()));

        DeliveryResponseDto responseDto = convertirADto(updatedDelivery);

        return responseDto;
    }

    public DeliveryResponseDto enCaminoDelivery(Long id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getRepartidor().getEmail()))
        {throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso: " + username + " " + delivery.getRepartidor().getEmail());}

        delivery.setStatus(StatusDelivery.EN_CAMINO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(delivery, delivery.getClient().getEmail()));

        return response;
    }

    public DeliveryResponseDto entregadoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getRepartidor().getEmail()))
        {throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso: " + username);}

        delivery.setStatus(StatusDelivery.ENTREGADO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(delivery, delivery.getClient().getEmail()));

        return response;
    }

    public DeliveryResponseDto listoDelivery(Long id) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));

        delivery.setStatus(StatusDelivery.LISTO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(delivery, delivery.getClient().getEmail()));

        return modelMapper.map(response, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto enPreparacionDelivery(Long id) {

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));

        delivery.setStatus(StatusDelivery.EN_PREPARACION);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(delivery, delivery.getClient().getEmail()));

        return modelMapper.map(response, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto canceladoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con " + id + " no encontrado"));

        // Verificar que el usuario autenticado sea el cliente que creó el delivery
        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Usuario no tiene permitido acceder a este recurso");
        }

        delivery.setStatus(StatusDelivery.CANCELADO);

        Delivery canceladoDelivery = deliveryRepository.save(delivery);

        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(canceladoDelivery, canceladoDelivery.getClient().getEmail()));

        return modelMapper.map(canceladoDelivery, DeliveryResponseDto.class);
    }

    public Page<DeliveryResponseDto> findCurrentDeliveries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<StatusDelivery> statuses = List.of(
                StatusDelivery.EN_PREPARACION,
                StatusDelivery.LISTO,
                StatusDelivery.EN_CAMINO,
                StatusDelivery.RECIBIDO
        );

        Page<Delivery> currentDeliveries = deliveryRepository.findByStatusIn(statuses, pageable);

        List<DeliveryResponseDto> deliveriesResponse = currentDeliveries.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return new PageImpl<>(deliveriesResponse, pageable, currentDeliveries.getTotalElements());
    }

    public DeliveryResponseDto convertirADto(Delivery delivery){
        DeliveryResponseDto responseDto = modelMapper.map(delivery, DeliveryResponseDto.class);
        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id1 : delivery.getIdProducts()) {
            productRepository.findById(id1).ifPresent(product -> {
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
               // delivery.setPrecio(delivery.getPrecio()+ product.getPrice());
            });
        }
        responseDto.setProducts(productos);
        return  responseDto;
    }
}


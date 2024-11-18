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
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.example.proydbp.repartidor.domain.RepartidorService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public List<DeliveryResponseDto> findAllDeliveries() {
        List<DeliveryResponseDto> deliveriesResponse = new ArrayList<>();

        for (Delivery delivery : deliveryRepository.findAll()) {

            DeliveryResponseDto responseDto =convertirADto(delivery);

            deliveriesResponse.add(responseDto);
        }
        return deliveriesResponse;
    }

    public DeliveryResponseDto createDelivery(DeliveryRequestDto dto) {

        // Verificación de autorización: Asegurarse que el usuario es un cliente
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        // Buscar al cliente en la base de datos usando su email
        Client cliente = clientRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con nombre de usuario " + username + " no encontrado"));

        // Mapeo del DTO a la entidad Delivery
        Delivery delivery = modelMapper.map(dto, Delivery.class);

        // Seteo de otros campos en el objeto Delivery
        delivery.setDireccion(dto.getDireccion());
        delivery.setDescripcion(dto.getDescripcion());
        delivery.setClient(cliente);  // Asociar el cliente al delivery
        delivery.setRepartidor(repartidorService.asignarRepartidor()); // Asignación del repartidor
        delivery.setStatus(StatusDelivery.RECIBIDO); // Estado inicial
        delivery.setFecha(ZonedDateTime.now()); // Fecha actual de creación
        delivery.setCostoDelivery(5.0); // Costo base de delivery (ajustar según lógica)
        delivery.setIdProducts(dto.getIdProducts()); // Asignar los ID de los productos
        delivery.setPrecio(0.0); // Inicializar el precio

        // Obtener los productos asociados y calcular el precio total
        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresent(product -> {
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
                delivery.setPrecio(delivery.getPrecio() + product.getPrice()); // Sumar el precio del producto
            });
        }

        // Guardar el delivery en la base de datos
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // Publicar el evento para notificar al cliente sobre el nuevo delivery
        eventPublisher.publishEvent(new DeliveryCrearEvent(savedDelivery, cliente.getEmail()));

        // Publicar el evento para notificar al repartidor sobre el nuevo delivery solo si tiene asignado un repartidor
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
                .orElseThrow(() -> new ResourceNotFoundException("Delivery con "+id+ " no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getClient().getEmail())) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!clientName.equals(delivery.getRepartidor().getEmail())) {
            throw new UnauthorizeOperationException("Cliente no autorizado");
        }

        delivery.setStatus(StatusDelivery.CANCELADO);

        Delivery canceladoDelivery = deliveryRepository.save(delivery);

        // Publicar evento de cambio de estado
        eventPublisher.publishEvent(new DeliveryEstadoChangeEvent(canceladoDelivery, canceladoDelivery.getClient().getEmail()));

        return modelMapper.map(canceladoDelivery, DeliveryResponseDto.class);
    }

    public List<DeliveryResponseDto> findCurrentDeliveries() {
        List<Delivery> currentDeliveries = deliveryRepository.findByStatusIn(
                List.of(StatusDelivery.EN_PREPARACION, StatusDelivery.LISTO, StatusDelivery.EN_CAMINO, StatusDelivery.RECIBIDO)
        );

        List<DeliveryResponseDto> deliveriesResponse = new ArrayList<>();
        for (Delivery delivery : currentDeliveries) {
            deliveriesResponse.add(convertirADto(delivery));
        }
        return deliveriesResponse;
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


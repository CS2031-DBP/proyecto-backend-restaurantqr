package com.example.proydbp.delivery.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.dto.PatchDeliveryDto;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.product.dto.ProductResponseDto;
import com.example.proydbp.product.infrastructure.ProductRepository;
import com.example.proydbp.repartidor.domain.Repartidor;
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
    public DeliveryService(DeliveryRepository deliveryRepository,
                           AuthorizationUtils authorizationUtils,
                           ApplicationEventPublisher eventPublisher,
                           ModelMapper modelMapper, @Lazy RepartidorService repartidorService, ClientRepository clientRepository, ProductRepository productRepository) {
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
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

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

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client cliente = clientRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with username " + username));

        Delivery delivery = new Delivery();
        delivery.setDireccion(dto.getDireccion());
        delivery.setDescripcion(dto.getDescripcion());
        delivery.setClient(cliente);
        delivery.setRepartidor(repartidorService.asignarRepartidor());
        delivery.setStatus(StatusDelivery.RECIBIDO);
        delivery.setFecha(ZonedDateTime.now());
        delivery.setCostoDelivery(5.0);
        delivery.setIdProducts(dto.getIdProducts());
        delivery.setPrecio(0.0);

        List<ProductResponseDto> productos = new ArrayList<>();
        for (Long id : dto.getIdProducts()) {
            productRepository.findById(id).ifPresent(product -> {
                ProductResponseDto productDto = modelMapper.map(product, ProductResponseDto.class);
                productos.add(productDto);
                delivery.setPrecio(delivery.getPrecio()+ product.getPrice());
            });
        }

        DeliveryResponseDto deliveryResponse = convertirADto(deliveryRepository.save(delivery));


        //Evento
        //String recipientEmail = savedDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryCreatedEvent(savedDelivery, recipientEmail));

        return deliveryResponse;
    }


    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        //String recipientEmail = "fernando.munoz.p@utec.edu.pe";
        //eventPublisher.publishEvent(new DeliveryDeletedEvent(delivery, recipientEmail));

        deliveryRepository.deleteById(id);
    }

    public DeliveryResponseDto updateDelivery(Long id, PatchDeliveryDto dto) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getClient().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");}


        delivery.setDescripcion(dto.getDescripcion());
        delivery.setDireccion(dto.getDireccion());

        Delivery updatedDelivery = deliveryRepository.save(delivery);


        DeliveryResponseDto responseDto = convertirADto(updatedDelivery);
        //Evento
        //String recipientEmail = updatedDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryUpdatedEvent(updatedDelivery, recipientEmail));

        return convertirADto(updatedDelivery);
    }


    public DeliveryResponseDto enCaminoDelivery(Long id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getRepartidor().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource: " + username + " " + delivery.getRepartidor().getEmail());}




        delivery.setStatus(StatusDelivery.EN_CAMINO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));
        //Evento
        //String recipientEmail = enRutaDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryEnRutaEvent(enRutaDelivery, recipientEmail));

        return response;
    }

    public DeliveryResponseDto entregadoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getRepartidor().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource: " + username);}

        delivery.setStatus(StatusDelivery.ENTREGADO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        //Evento
        //String recipientEmail = entregadoDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryEntregadoEvent(entregadoDelivery, recipientEmail));

        return response;
    }


    public DeliveryResponseDto listoDelivery(Long id) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.LISTO);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        //String recipientEmail = listoDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryListoEvent(listoDelivery, recipientEmail));

        return modelMapper.map(response, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto enPreparacionDelivery(Long id) {

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.EN_PREPARACION);

        DeliveryResponseDto response = convertirADto(deliveryRepository.save(delivery));

        //Evento
        //String recipientEmail = enPreparacionDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryPreparandoEvent(enPreparacionDelivery, recipientEmail));

        return modelMapper.map(response, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto canceladoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String username = authorizationUtils.getCurrentUserEmail();
        if (!Objects.equals(username, delivery.getClient().getEmail()))
        {throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");}

        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!clientName.equals(delivery.getRepartidor().getEmail())) {
            throw new UnauthorizeOperationException("Client no authorized");
        }

        delivery.setStatus(StatusDelivery.CANCELADO);

        Delivery canceladoDelivery = deliveryRepository.save(delivery);

        //String recipientEmail = canceladoDelivery.getRepartidor().getEmail();
        //eventPublisher.publishEvent(new DeliveryCanceladoEvent(canceladoDelivery, recipientEmail));

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


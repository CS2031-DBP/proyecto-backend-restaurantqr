package com.example.proydbp.delivery.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.repartidor.domain.RepartidorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    final private DeliveryRepository deliveryRepository;
    final private ApplicationEventPublisher eventPublisher;
    final private ModelMapper modelMapper;
    private final RepartidorService repartidorService;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository,
                           ApplicationEventPublisher eventPublisher,
                           ModelMapper modelMapper, RepartidorService repartidorService) {
        this.deliveryRepository = deliveryRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
        this.repartidorService = repartidorService;
    }

    public DeliveryResponseDto findDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        return modelMapper.map(delivery, DeliveryResponseDto.class);
    }

    public List<DeliveryResponseDto> findAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }

    public DeliveryResponseDto createDelivery(DeliveryRequestDto dto) {
        Delivery delivery = modelMapper.map(dto, Delivery.class);
        delivery.setRepartidor(repartidorService.asignarRepartidor());
        delivery.setStatus(StatusDelivery.RECIBIDO);
        delivery.setFecha(LocalDate.now());
        delivery.setHora(LocalTime.now());
        delivery.setCostoDelivery(5.0); // Establecer costo de delivery como double

        // Calcular el precio total sumando el costo de delivery y el precio de todas las órdenes
        double totalOrdersPrice = delivery.getOrder().stream()
                .mapToDouble(Order::getPrice) // Obtener el precio de cada orden como double
                .sum(); // Sumar los precios

        // Establecer el precio total
        delivery.setPrecio(totalOrdersPrice + delivery.getCostoDelivery()); // Sumar costo de delivery

        Delivery savedDelivery = deliveryRepository.save(delivery);

        String recipientEmail = savedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryCreatedEvent(savedDelivery, recipientEmail));

        return modelMapper.map(savedDelivery, DeliveryResponseDto.class);
    }



    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String recipientEmail = "fernando.munoz.p@utec.edu.pe";
        //eventPublisher.publishEvent(new DeliveryDeletedEvent(id, delivery, recipientEmail));

        deliveryRepository.deleteById(id);
    }

    public DeliveryResponseDto updateDelivery(Long id, DeliveryRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        modelMapper.map(dto, delivery);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        String recipientEmail = savedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryUpdatedEvent(savedDelivery, recipientEmail));
        return modelMapper.map(savedDelivery, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto enCaminoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.EN_CAMINO);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        String recipientEmail = updatedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryEnRutaEvent(updatedDelivery, recipientEmail));

        return modelMapper.map(updatedDelivery, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto entregadoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.ENTREGADO);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        String recipientEmail = updatedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryEntregadoEvent(updatedDelivery, recipientEmail));

        return modelMapper.map(updatedDelivery, DeliveryResponseDto.class);
    }

    public List<DeliveryResponseDto> findCurrentDeliveries() {
        List<Delivery> currentDeliveries = deliveryRepository.findByStatusIn(List.of(StatusDelivery.EN_PREPARACION,
                StatusDelivery.LISTO, StatusDelivery.EN_CAMINO, StatusDelivery.RECIBIDO));

        return currentDeliveries.stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }


    public DeliveryResponseDto listoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.LISTO);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        String recipientEmail = updatedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryEnRutaEvent(updatedDelivery, recipientEmail));

        return modelMapper.map(updatedDelivery, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto enPreparacionDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        delivery.setStatus(StatusDelivery.EN_PREPARACION);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        String recipientEmail = updatedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryEnRutaEvent(updatedDelivery, recipientEmail));

        return modelMapper.map(updatedDelivery, DeliveryResponseDto.class);
    }

    public DeliveryResponseDto canceladoDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery no encontrado"));

        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!clientName.equals(delivery.getRepartidor().getEmail())) {
            throw new UnauthorizeOperationException("Client no authorized");
        }

        delivery.setStatus(StatusDelivery.CANCELADO);

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        String recipientEmail = updatedDelivery.getRepartidor().getEmail();

        //eventPublisher.publishEvent(new DeliveryEnRutaEvent(updatedDelivery, recipientEmail));

        return modelMapper.map(updatedDelivery, DeliveryResponseDto.class);
    }
}

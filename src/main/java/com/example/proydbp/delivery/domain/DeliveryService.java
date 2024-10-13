package com.example.proydbp.delivery.domain;

import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.delivery.domain.Status;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    final private DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.modelMapper = modelMapper;
    }

    public DeliveryResponseDto findDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con ID: \" + id"));
        return modelMapper.map(delivery, DeliveryResponseDto.class);
    }

    public List<DeliveryResponseDto> findAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .toList();
    }

    public DeliveryResponseDto createDelivery(DeliveryRequestDto dto) {
        Delivery delivery = mapToEntity(dto);
        return mapToResponseDto(deliveryRepository.save(delivery));
    }

    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con ID: " + id));
        deliveryRepository.delete(delivery);
    }

    public DeliveryResponseDto updateDelivery(Long id, DeliveryRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con ID: " + id));

        if (dto.getDireccion() != null){
            delivery.setDireccion(dto.getDireccion());
        }

        if (dto.getCostoDelivery() != null){
            delivery.setCostoDelivery(dto.getCostoDelivery());
        }

        deliveryRepository.save(delivery);
        return mapToResponseDto(delivery);
    }

    public void endDeliveryPreparando(Long id){
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrado con ID: " + id));
        delivery.setStatus(Status.EN_PREPARACION);
        deliveryRepository.save(delivery);
    }

    public void endDeliveryListo(Long id){
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrado con ID: " + id));
        delivery.setStatus(Status.LISTO);
        deliveryRepository.save(delivery);
    }

    public List<DeliveryResponseDto> findDeliveriesRecibidos(){
        return deliveryRepository.findByStatus(Status.RECIBIDO).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private Delivery mapToEntity(DeliveryRequestDto dto) {
        Delivery delivery = new Delivery();
        delivery.setDireccion(dto.getDireccion());
        delivery.setCostoDelivery(dto.getCostoDelivery());
        delivery.setFecha(dto.getFecha());
        delivery.setHora(dto.getHora());
        delivery.setStatus(Status.RECIBIDO);
        delivery.setPrecio(dto.getPrecio());
        return delivery;
    }

    private DeliveryResponseDto mapToResponseDto(Delivery delivery) {
        //return new DeliveryResponseDto(delivery.getId(), delivery.getClient(), delivery.getDireccion(),
        //        delivery.getCostoDelivery(), delivery.getFecha(), delivery.getHora(), delivery.getStatus(), delivery.getOrder(), delivery.getRepartidor(), delivery.getPrecio());
        DeliveryResponseDto responseDto = new DeliveryResponseDto();
        responseDto.setId(delivery.getId());
        responseDto.setDireccion(delivery.getDireccion());
        responseDto.setCostoDelivery(delivery.getCostoDelivery());
        responseDto.setFecha(delivery.getFecha());
        responseDto.setHora(delivery.getHora());
        responseDto.setPrecio(delivery.getPrecio());
        return responseDto;
    }

}

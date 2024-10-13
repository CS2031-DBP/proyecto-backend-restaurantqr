package com.example.proydbp.repartidor.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.user.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final DeliveryRepository deliveryRepository; // Nueva inyección
    private final ModelMapper modelMapper;

    @Autowired
    public RepartidorService(RepartidorRepository repartidorRepository, DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.repartidorRepository = repartidorRepository;
        this.deliveryRepository = deliveryRepository; // Inicializa el DeliveryRepository
        this.modelMapper = modelMapper;
    }


    public RepartidorResponseDto findRepartidorById(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));
        return modelMapper.map(repartidor, RepartidorResponseDto.class);
    }


    public List<RepartidorResponseDto> findAllRepartidors() {
        List<Repartidor> repartidores = repartidorRepository.findAll();
        return repartidores.stream()
                .map(repartidor -> modelMapper.map(repartidor, RepartidorResponseDto.class))
                .collect(Collectors.toList());
    }


    public RepartidorResponseDto createRepartidor(RepartidorRequestDto dto) {

        Repartidor repartidor = modelMapper.map(dto, Repartidor.class);
        repartidor.setRole(Role.REPARTIDOR);
        Repartidor savedRepartidor = repartidorRepository.save(repartidor);
        return modelMapper.map(savedRepartidor, RepartidorResponseDto.class);
    }


    public void deleteRepartidor(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));
        repartidorRepository.delete(repartidor);
    }


    public RepartidorResponseDto updateRepartidor(Long id, PatchRepartidorDto dto) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));


        Repartidor updatedRepartidor = repartidorRepository.save(repartidor);
        return modelMapper.map(updatedRepartidor, RepartidorResponseDto.class);
    }


    public RepartidorResponseDto findAuthenticatedRepartidor(Long id) {
        return findRepartidorById(id); // Aquí podrías agregar lógica para el usuario autenticado
    }


    public List<DeliveryResponseDto> findDeliverysActuales(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));

        List<Delivery> deliverys = repartidor.getDeliverys().stream()
                .filter(delivery -> delivery.getEstado().equals("LISTO"))
                .collect(Collectors.toList());

        return deliverys.stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }


    public void enCaminoDelivery(Long idDelivery) {
            //Logica para saber si es el repartidor autenticado
        Delivery delivery= deliveryRepository.findById(idDelivery);
        delivery.setEstado(estado.EN_CAMINO);
    }


    public void endDelivery(Long idDelivery) {
        Delivery delivery= deliveryRepository.findById(idDelivery);
        delivery.setEstado(estado.ENTREGADO);
    }

    public Repartidor asignarRepartidor() {
        // Obtén todos los repartidores
        List<Repartidor> repartidores = repartidorRepository.findAll();

        // Encuentra el repartidor con la menor cantidad de deliverys en total
        Optional<Repartidor> repartidorAsignado = repartidores.stream()
                .min(Comparator.comparingInt(repartidor ->
                        repartidor.getDeliverys().size())); // Contar todos los deliverys

        // Si se encuentra un repartidor, devuélvelo, de lo contrario, puedes lanzar una excepción o manejarlo como desees
        return repartidorAsignado.orElseThrow(() -> new RuntimeException("No hay repartidores disponibles"));
    }

    public void updateRatingScore(Long repartidorId) {
        Repartidor repartidor = repartidorRepository.findById(repartidorId)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + repartidorId));

        double averageRating = repartidor.getReviewsRepartidor().stream()
                .mapToDouble(ReviewDelivery::getRating) //
                .average()
                .orElse(0.0); // Si no hay reseñas, el promedio es 0

        repartidor.setRatingScore(averageRating);
        repartidorRepository.save(repartidor);
    }








}
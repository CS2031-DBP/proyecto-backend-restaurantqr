package com.example.proydbp.repartidor.domain;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

        repartidor.setEmail(dto.getEmail());
        repartidor.setFirstName(dto.getFirstName());
        repartidor.setLastName(dto.getLastName());
        repartidor.setPhoneNumber(dto.getPhoneNumber());
        repartidor.setPassword(dto.getPassword());

        Repartidor updatedRepartidor = repartidorRepository.save(repartidor);
        return modelMapper.map(updatedRepartidor, RepartidorResponseDto.class);
    }


    public RepartidorResponseDto findAuthenticatedRepartidor(Long id) {
        // Here get the current user identifier (email) using Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Repartidor repartidor = repartidorRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(repartidor, RepartidorResponseDto.class);
    }


    public List<DeliveryResponseDto> findDeliverysActuales() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with username " + username));

        return repartidor.getDeliveries().stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusDelivery.LISTO ||
                                pedido.getStatus() == StatusDelivery.EN_CAMINO)
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }


    // adicional

    private int countPedidosEnEstado(Repartidor repartidor) {
        return (int) repartidor.getDeliveries().stream()
                .filter(pedido -> pedido.getStatus() == StatusDelivery.EN_CAMINO || pedido.getStatus() == StatusDelivery.LISTO)
                .count();
    }

    public Repartidor asignarRepartidor() {
        return repartidorRepository.findAll().stream()
                .min((m1, m2) -> Integer.compare(
                        countPedidosEnEstado(m1),
                        countPedidosEnEstado(m2)))
                .orElseThrow(() -> new IllegalStateException("No hay repartidores disponibles"));
    }

    public void updateRatingScore(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

        // Actualiza el ratingScore promedio a partir de las reseñas
        double promedio = repartidor.getReviewsRepartidor().stream()
                .mapToDouble(ReviewDelivery::getRatingScore)
                .average()
                .orElse(0.0);
        repartidor.setRatingScore(promedio);

        repartidorRepository.save(repartidor);
    }


    public List<ReviewDeliveryResponseDto> findMisReviews(Long id){
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

        return repartidor.getReviewsRepartidor().stream()
                .map(review -> modelMapper.map(review, ReviewDeliveryResponseDto.class))
                .toList();
    }

}
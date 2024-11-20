package com.example.proydbp.repartidor.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.events.email_event.PerfilUpdateRepartidorEvent;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.dto.RepartidorSelfResponseDto;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class RepartidorService {

    private final RepartidorRepository repartidorRepository;
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationUtils authorizationUtils;
    private final DeliveryService deliveryService;
    private final BaseUserRepository baseUserRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RepartidorService(PasswordEncoder passwordEncoder, BaseUserRepository baseUserRepository,
                             @Lazy DeliveryService deliveryService, AuthorizationUtils authorizationUtils,
                             RepartidorRepository repartidorRepository, DeliveryRepository deliveryRepository,
                             ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.repartidorRepository = repartidorRepository;
        this.deliveryRepository = deliveryRepository;
        this.baseUserRepository = baseUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.deliveryService = deliveryService;
        this.eventPublisher = eventPublisher;
    }

    public RepartidorResponseDto findRepartidorById(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con " + id + " no encontrado" ));
        return convertirADto(repartidor);
    }

    public List<RepartidorResponseDto> findAllRepartidors() {
        List<Repartidor> repartidores = repartidorRepository.findAll();
        List<RepartidorResponseDto> repartidorResponseDtos = new ArrayList<>();
        for(Repartidor repartidor : repartidores) {
            repartidorResponseDtos.add(convertirADto(repartidor));
        }
        return repartidorResponseDtos;
    }

    public RepartidorResponseDto createRepartidor(RepartidorRequestDto dto) {

        Optional<User> user = baseUserRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        Repartidor repartidor = new Repartidor();
        repartidor.setCreatedAt(ZonedDateTime.now());
        repartidor.setRole(Role.REPARTIDOR);
        repartidor.setFirstName(dto.getFirstName());
        repartidor.setLastName(dto.getLastName());
        repartidor.setEmail(dto.getEmail());
        repartidor.setPassword(passwordEncoder.encode(dto.getPassword()));
        repartidor.setPhoneNumber(dto.getPhone());
        repartidor.setUpdatedAt(ZonedDateTime.now());
        repartidor.setRatingScore(0.0);
        repartidor.setReviewDeliveries(new ArrayList<>());
        repartidor.setDeliveries(new ArrayList<>());
        return convertirADto(repartidorRepository.save(repartidor));
    }

    public void deleteRepartidor(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con " + id + " no encontrado" ));
        repartidorRepository.delete(repartidor);
    }

    public RepartidorSelfResponseDto updateRepartidor(Long id, PatchRepartidorDto dto) {
        // Buscar el repartidor por su ID
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con " + id + " no encontrado"));

        // Map para registrar los campos que se actualizan
        Map<String, String> updatedFields = new HashMap<>();

        // Comparar y actualizar solo los campos proporcionados
        if (dto.getFirstName() != null && !dto.getFirstName().equals(repartidor.getFirstName())) {
            updatedFields.put("Nombre", dto.getFirstName());
            repartidor.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().equals(repartidor.getLastName())) {
            updatedFields.put("Apellido", dto.getLastName());
            repartidor.setLastName(dto.getLastName());
        }

        if (dto.getPassword() != null) {
            updatedFields.put("Contraseña", "Actualizada");
            repartidor.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPhone() != null && !dto.getPhone().equals(repartidor.getPhoneNumber())) {
            updatedFields.put("Teléfono", dto.getPhone());
            repartidor.setPhoneNumber(dto.getPhone());
        }

        // Actualizar la fecha de modificación
        repartidor.setUpdatedAt(ZonedDateTime.now());

        // Guardar el repartidor actualizado en el repositorio
        Repartidor updatedRepartidor = repartidorRepository.save(repartidor);

        // Publicar evento con los campos actualizados
        eventPublisher.publishEvent(new PerfilUpdateRepartidorEvent(updatedRepartidor, updatedFields, updatedRepartidor.getEmail()));

        // Convertir el repartidor actualizado a DTO y retornarlo
        return modelMapper.map(updatedRepartidor, RepartidorSelfResponseDto.class);
    }

    public RepartidorSelfResponseDto findAuthenticatedRepartidor() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Repartidor repartidor = repartidorRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(repartidor, RepartidorSelfResponseDto.class);
    }

    public List<DeliveryResponseDto> findDeliverysActuales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con nombre de usuario " + username + " no encontrado" ));

        List<DeliveryResponseDto> deliverys = new ArrayList<>();
        for(Delivery delivery : repartidor.getDeliveries()){
            if(delivery.getStatus() != StatusDelivery.ENTREGADO && delivery.getStatus() != StatusDelivery.CANCELADO){
                DeliveryResponseDto deliveryDto = deliveryService.convertirADto(delivery);
                deliverys.add(deliveryDto);
            }
        }
        return deliverys;
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
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con " + id + " no encontrado"));

        // Actualiza el ratingScore promedio a partir de las reseñas
        double promedio = repartidor.getReviewDeliveries().stream()
                .mapToDouble(ReviewDelivery::getRatingScore)
                .average()
                .orElse(0.0);
        repartidor.setRatingScore(promedio);

        repartidorRepository.save(repartidor);
    }

    public List<ReviewDeliveryResponseDto> findMisReviews(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con nombre de usuario " + username + " no encontrado"));

        List<ReviewDelivery> reviews = Optional.ofNullable(repartidor.getReviewDeliveries()).orElse(Collections.emptyList());

        return reviews.stream()
                .map(review -> modelMapper.map(review,ReviewDeliveryResponseDto.class)).toList();
    }

    public RepartidorSelfResponseDto updateAuthenticatedRepartidor(PatchRepartidorDto dto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con nombre de usuario " + username + " no encontrado"));

        repartidor.setUpdatedAt(ZonedDateTime.now());
        repartidor.setFirstName(dto.getFirstName());
        repartidor.setLastName(dto.getLastName());
        repartidor.setPassword(passwordEncoder.encode(dto.getPassword()));
        repartidor.setPhoneNumber(dto.getPhone());
        return modelMapper.map( repartidorRepository.save(repartidor), RepartidorSelfResponseDto.class);
    }

    public List<DeliveryResponseDto> findDeliverys() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor con nombre de usuario " + username + " no encontrado"));

        List<DeliveryResponseDto> deliverys = new ArrayList<>();
        for(Delivery delivery : repartidor.getDeliveries()){
                DeliveryResponseDto deliveryDto = deliveryService.convertirADto(delivery);
                deliverys.add(deliveryDto);
        }
        return deliverys;
    }

    public RepartidorResponseDto convertirADto(Repartidor repartidor) {

        RepartidorResponseDto repartidorDto = modelMapper.map(repartidor, RepartidorResponseDto.class);

        List<DeliveryResponseDto> deliveriesResponse = new ArrayList<>();
        for (Delivery delivery : repartidor.getDeliveries()) {
            deliveriesResponse.add(deliveryService.convertirADto(delivery));
        }
        repartidorDto.setDeliveries(deliveriesResponse);

        return repartidorDto;
    }
}
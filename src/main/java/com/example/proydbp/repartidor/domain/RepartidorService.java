package com.example.proydbp.repartidor.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
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

    @Autowired
    public RepartidorService(PasswordEncoder passwordEncoder,BaseUserRepository baseUserRepository , @Lazy DeliveryService deliveryService, AuthorizationUtils authorizationUtils , RepartidorRepository repartidorRepository, DeliveryRepository deliveryRepository, ModelMapper modelMapper) {
        this.repartidorRepository = repartidorRepository;
        this.deliveryRepository = deliveryRepository;
        this.baseUserRepository = baseUserRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.deliveryService = deliveryService;
    }


    public RepartidorResponseDto findRepartidorById(Long id) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));
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
        if (user.isPresent()) throw new UserAlreadyExistException("Email is already registered");

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
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));
        repartidorRepository.delete(repartidor);
    }


    public RepartidorSelfResponseDto updateRepartidor(Long id, PatchRepartidorDto dto) {
        Repartidor repartidor = repartidorRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with id " + id));

        repartidor.setUpdatedAt(ZonedDateTime.now());
        repartidor.setFirstName(dto.getFirstName());
        repartidor.setLastName(dto.getLastName());
        repartidor.setPassword(passwordEncoder.encode(dto.getPassword()));
        repartidor.setPhoneNumber(dto.getPhone());

        return modelMapper.map( repartidorRepository.save(repartidor), RepartidorSelfResponseDto.class);
    }


    public RepartidorSelfResponseDto findAuthenticatedRepartidor() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Repartidor repartidor = repartidorRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(repartidor, RepartidorSelfResponseDto.class);
    }


    public List<DeliveryResponseDto> findDeliverysActuales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");


        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with username " + username));

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
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

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
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

        List<ReviewDelivery> reviews = Optional.ofNullable(repartidor.getReviewDeliveries()).orElse(Collections.emptyList());


        return reviews.stream()
                .map(review -> modelMapper.map(review,ReviewDeliveryResponseDto.class)).toList();
    }

    public RepartidorSelfResponseDto updateAuthenticatedRepartidor(PatchRepartidorDto dto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");
        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

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
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");
        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

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
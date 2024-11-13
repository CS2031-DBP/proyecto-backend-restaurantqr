package com.example.proydbp.mesero.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.PedidoLocalService;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeseroService {

    private final MeseroRepository meseroRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationUtils authorizationUtils;
    private final PedidoLocalService pedidoLocalService;
    private final BaseUserRepository baseUserRepository;


    @Autowired
    public MeseroService(MeseroRepository meseroRepository, BaseUserRepository baseUserRepository, AuthorizationUtils authorizationUtils , ModelMapper modelMapper, PasswordEncoder passwordEncoder, PedidoLocalService pedidoLocalService) {
        this.meseroRepository = meseroRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.pedidoLocalService = pedidoLocalService;
        this.baseUserRepository = baseUserRepository;
    }


    public MeseroResponseDto findMeseroById(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return convertirADto(mesero);
    }

    public List<MeseroResponseDto> findAllMeseros() {
        List<Mesero> meseros = meseroRepository.findAll();
        List<MeseroResponseDto> meseroResponseDtos = new ArrayList<>();
        for(Mesero meserodt : meseros) {
            meseroResponseDtos.add(convertirADto(meserodt));
        }
        return meseroResponseDtos;
    }

    public MeseroResponseDto createMesero(MeseroRequestDto dto) {

        Optional<User> user = baseUserRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("Email is already registered");

        Mesero mesero = new Mesero();
        mesero.setCreatedAt(ZonedDateTime.now());
        mesero.setRole(Role.MESERO);
        mesero.setFirstName(dto.getFirstName());
        mesero.setLastName(dto.getLastName());
        mesero.setEmail(dto.getEmail());
        mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        mesero.setPhoneNumber(dto.getPhone());
        mesero.setUpdatedAt(ZonedDateTime.now());
        mesero.setRatingScore(0.0);
        mesero.setReviewMeseros(new ArrayList<>());
        return convertirADto(meseroRepository.save(mesero));
    }

    public void deleteMesero(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        meseroRepository.delete(mesero);
    }

    public MeseroSelfResponseDto updateMesero(Long id, PatchMeseroDto dto) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        mesero.setUpdatedAt(ZonedDateTime.now());
        mesero.setFirstName(dto.getFirstName());
        mesero.setLastName(dto.getLastName());
        mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        mesero.setPhoneNumber(dto.getPhone());

        return modelMapper.map( meseroRepository.save(mesero), MeseroSelfResponseDto.class);
    }




    public List<PedidoLocalResponseDto> findMisPedidosLocalesActuales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");


        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with username " + username));

        List<PedidoLocalResponseDto> pedidos = new ArrayList<>();
        for(PedidoLocal pedidoLocal : mesero.getPedidosLocales()){
            if(pedidoLocal.getStatus() != StatusPedidoLocal.ENTREGADO){
                PedidoLocalResponseDto pedidoDto = pedidoLocalService.convertirADto(pedidoLocal);
                pedidos.add(pedidoDto);
            }

        }

        return pedidos;
    }


    public MeseroSelfResponseDto getMeseroOwnInfo() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Mesero mesero = meseroRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(mesero, MeseroSelfResponseDto.class);

    }




    public List<ReviewMeseroResponseDto> findMisReviews() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        List<ReviewMesero> reviewMesero = Optional.ofNullable(mesero.getReviewMeseros()).orElse(Collections.emptyList());


        return reviewMesero.stream()
                .map(review -> modelMapper.map(review, ReviewMeseroResponseDto.class)).toList();
    }

    public List<PedidoLocalResponseDto> findPedidosLocales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor not found with username " + username));

        List<PedidoLocalResponseDto> pedidos = new ArrayList<>();
        for(PedidoLocal pedidoLocal : mesero.getPedidosLocales()){
                PedidoLocalResponseDto pedidoDto = pedidoLocalService.convertirADto(pedidoLocal);
                pedidos.add(pedidoDto);

        }

        return pedidos;

    }


    public MeseroSelfResponseDto updateAuthenticatedMesero(PatchClientDto dto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");
        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        mesero.setUpdatedAt(ZonedDateTime.now());
        mesero.setFirstName(dto.getFirstName());
        mesero.setLastName(dto.getLastName());
        mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        mesero.setPhoneNumber(dto.getPhone());
        return modelMapper.map( meseroRepository.save(mesero), MeseroSelfResponseDto.class);
    }






    // Métodos adicionales
    public void updateRatingScore(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        // Actualiza el ratingScore promedio a partir de las reseñas
        double promedio = mesero.getReviewMeseros().stream()
                .mapToDouble(ReviewMesero::getRatingScore)
                .average()
                .orElse(0.0);
        mesero.setRatingScore(promedio);

        meseroRepository.save(mesero);
    }

    public Mesero asignarMesero() {
        return meseroRepository.findAll().stream()
                .min((m1, m2) -> Integer.compare(
                        countPedidosEnEstado(m1),
                        countPedidosEnEstado(m2)))
                .orElseThrow(() -> new IllegalStateException("No hay meseros disponibles"));
    }

    private int countPedidosEnEstado(Mesero mesero) {
        return (int) mesero.getPedidosLocales().stream()
                .filter(pedido -> pedido.getStatus() == StatusPedidoLocal.EN_PREPARACION || pedido.getStatus() == StatusPedidoLocal.LISTO)
                .count();
    }


    public MeseroResponseDto convertirADto(Mesero mesero) {

        MeseroResponseDto meseroResponseDto = modelMapper.map(mesero, MeseroResponseDto.class);
        meseroResponseDto.setId(mesero.getId());
        List<PedidoLocalResponseDto> pedidosResponse = new ArrayList<>();
        for (PedidoLocal pedidoLocal : mesero.getPedidosLocales()) {
            pedidosResponse.add(pedidoLocalService.convertirADto(pedidoLocal));
        }
        meseroResponseDto.setPedidosLocales(pedidosResponse);

        return meseroResponseDto;
    }











}

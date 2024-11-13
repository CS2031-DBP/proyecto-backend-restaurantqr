package com.example.proydbp.mesero.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public MeseroService(MeseroRepository meseroRepository, AuthorizationUtils authorizationUtils , ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.meseroRepository = meseroRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
    }


    public MeseroResponseDto findMeseroById(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(mesero, MeseroResponseDto.class);
    }

    public List<MeseroResponseDto> findAllMeseros() {
        return meseroRepository.findAll().stream()
                .map(mesero -> modelMapper.map(mesero, MeseroResponseDto.class))
                .collect(Collectors.toList());
    }

    public MeseroResponseDto createMesero(MeseroRequestDto dto) {
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
        return modelMapper.map(meseroRepository.save(mesero), MeseroResponseDto.class);
    }

    public void deleteMesero(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        meseroRepository.delete(mesero);
    }

    public MeseroResponseDto updateMesero(Long id, PatchMeseroDto dto) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        mesero.setUpdatedAt(ZonedDateTime.now());
        mesero.setFirstName(dto.getFirstName());
        mesero.setLastName(dto.getLastName());
        mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        mesero.setPhoneNumber(dto.getPhone());

        return modelMapper.map( meseroRepository.save(mesero), MeseroResponseDto.class);
    }




    public List<PedidoLocalResponseDto> findMisPedidosLocalesActuales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        List<PedidoLocal> pedidosLocales = Optional.ofNullable(mesero.getPedidosLocales()).orElse(Collections.emptyList());

        return pedidosLocales.stream()
                .filter(pedido -> pedido.getStatus() == StatusPedidoLocal.LISTO || pedido.getStatus() == StatusPedidoLocal.EN_PREPARACION)
                .map(pedido -> modelMapper.map(pedido, PedidoLocalResponseDto.class)).toList();
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
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        List<PedidoLocal> pedidos = Optional.ofNullable(mesero.getPedidosLocales()).orElse(Collections.emptyList());


        return pedidos.stream()
                .map(pedido -> modelMapper.map(pedido, PedidoLocalResponseDto.class)).toList();

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
}

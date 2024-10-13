package com.example.proydbp.mesero.domain;

import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeseroService {

    final private MeseroRepository meseroRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeseroService(MeseroRepository meseroRepository) {
        this.meseroRepository = meseroRepository;
        this.modelMapper = new ModelMapper();
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
        Mesero mesero = modelMapper.map(dto, Mesero.class);
        mesero.setRole(Role.MESERO);
        mesero.setRatingScore(0.0);
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

        modelMapper.map(dto, mesero);
        Mesero updatedMesero = meseroRepository.save(mesero);

        return modelMapper.map(updatedMesero, MeseroResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesActuales(Long idMesero) {
        Mesero mesero = meseroRepository.findById(idMesero)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return mesero.getPedidosLocales().stream()
                .filter(pedido ->
                        pedido.getStatus() == StatusPedidoLocal.LISTO ||
                                pedido.getStatus() == StatusPedidoLocal.EN_PREPARACION)
                .map(pedido -> modelMapper.map(pedido, PedidoLocalResponseDto.class))
                .toList();
    }

    public MeseroSelfResponseDto getMeseroOwnInfo() {
        // Here get the current user identifier (email) using Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Mesero mesero = meseroRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return modelMapper.map(mesero, MeseroSelfResponseDto.class);

    }

    // Métodos adicionales
    public void updateRatingScore(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        // Actualiza el ratingScore promedio a partir de las reseñas
        double promedio = mesero.getReviewMesero().stream()
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


    public List<ReviewMeseroResponseDto> findMisReviews(Long id){
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        return mesero.getReviewMesero().stream()
                .map(review -> modelMapper.map(review, ReviewMeseroResponseDto.class))
                .toList();
    }

}

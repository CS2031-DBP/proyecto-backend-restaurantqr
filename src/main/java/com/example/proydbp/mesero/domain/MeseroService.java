package com.example.proydbp.mesero.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.orden.dto.OrderResponseDto;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
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
    final private PedidoLocalRepository pedidoLocalRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MeseroService(MeseroRepository meseroRepository, PedidoLocalRepository pedidoLocalRepository) {
        this.meseroRepository = meseroRepository;
        this.pedidoLocalRepository = pedidoLocalRepository;
        this.modelMapper = new ModelMapper();
    }

    public MeseroResponseDto findMeseroById(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        return mapToResponseDto(mesero);
    }

    public List<MeseroResponseDto> findAllMeseros() {
        return meseroRepository.findAll().stream().map(this::mapToResponseDto).toList();
    }

    public MeseroResponseDto createMesero(MeseroRequestDto dto) {
        Mesero mesero = mapToEntity(dto);
        return mapToResponseDto(meseroRepository.save(mesero));
    }

    public void deleteMesero(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        meseroRepository.delete(mesero);
    }

    public MeseroResponseDto updateMesero(Long id, PatchMeseroDto dto) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        if (dto.getRatingScore() != null) {
            mesero.setRatingScore(dto.getRatingScore());
        }
        return mapToResponseDto(mesero);
    }

    public List<PedidoLocalResponseDto> findPedidosLocalesActuales(Long idMesero) {
        Mesero mesero = meseroRepository.findById(idMesero)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));
        return mesero.getPedidosLocales().stream()
                .filter(pedido -> pedido.getEstado().equals("LISTO") || pedido.getEstado().equals("RECIBIDO"))
                .map(this::mapToPedidoLocalResponseDto)
                .toList();
    }

    public void pedidoLocalListo(Long idPedidoLocal) {
        PedidoLocal pedido = pedidoLocalRepository.findById(idPedidoLocal)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido local no encontrado"));
        pedido.setEstado("LISTO");
    }

    public void pedidoLocalEntregado(Long idPedidoLocal) {
        PedidoLocal pedido = pedidoLocalRepository.findById(idPedidoLocal)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido local no encontrado"));
        pedido.setEstado("ENTREGADO");
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
    public void updateRatingScore(PatchMeseroDto patchMeseroDto, Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero no encontrado"));

        // Actualiza el ratingScore si se proporciona en el DTO
        if (patchMeseroDto.getRatingScore() != null) {
            mesero.setRatingScore(patchMeseroDto.getRatingScore());
        }

        // Actualiza el ratingScore promedio a partir de las reseñas
        double promedio = mesero.getReviewsMesero().stream()
                .mapToDouble(ReviewMesero::getRating)
                .average()
                .orElse(0.0);
        mesero.setRatingScore(promedio);

        meseroRepository.save(mesero);
    }


    public Mesero asignarMesero() {
        return meseroRepository.findAll().stream()
                .min((m1, m2) -> Integer.compare(m1.getPedidosLocales().size(), m2.getPedidosLocales().size()))
                .orElseThrow(() -> new IllegalStateException("No hay meseros disponibles"));
    }

    private MeseroResponseDto mapToResponseDto(Mesero mesero) {
        return new MeseroResponseDto(mesero.getId(), mesero.getFirstName(),
                mesero.getLastName(), mesero.getPedidosLocales(),
                mesero.getReviewsMesero(), mesero.getRatingScore());
    }

    private Mesero mapToEntity(MeseroRequestDto dto) {
        Mesero mesero = new Mesero();
        mesero.setPedidosLocales(dto.getPedidosLocales());
        mesero.setReviewsMesero(dto.getReviewsMesero());
        mesero.setRatingScore(dto.getRatingScore());
        return mesero;
    }

    private PedidoLocalResponseDto mapToPedidoLocalResponseDto(PedidoLocal pedido) {
        PedidoLocalResponseDto responseDto = new PedidoLocalResponseDto();

        responseDto.setId(pedido.getId());
        responseDto.setFecha(pedido.getFecha());
        responseDto.setHora(pedido.getHora());
        responseDto.setEstado(pedido.getEstado());
        responseDto.setPrecio(pedido.getPrecio());
        responseDto.setTipoPago(pedido.getTipoPago());

        // Mapea la lista de órdenes si existe
        List<OrderResponseDto> ordersDto = pedido.getOrdenes()
                .stream()
                .map(order -> new OrderResponseDto(
                        order.getId(),
                        order.getPrice(),
                        order.getProducts(),
                        order.getDetails()
                                ))
                .collect(Collectors.toList());

        responseDto.setOrdenes(ordersDto);

        // Mapea el mesero asignado al pedido
        Mesero mesero = pedido.getMesero();
        MeseroResponseDto meseroDto = new MeseroResponseDto();
        meseroDto.setId(mesero.getId());
        meseroDto.setFirstName(mesero.getFirstName());
        meseroDto.setLastName(mesero.getLastName());

        responseDto.setMesero(meseroDto);

        return responseDto;
    }

}

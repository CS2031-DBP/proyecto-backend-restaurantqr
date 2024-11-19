package com.example.proydbp.mesero.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.events.email_event.BienvenidaMeseroEvent;
import com.example.proydbp.events.email_event.PerfilUpdateMeseroEvent;
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
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class MeseroService {

    private final MeseroRepository meseroRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationUtils authorizationUtils;
    private final PedidoLocalService pedidoLocalService;
    private final BaseUserRepository baseUserRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public MeseroService(MeseroRepository meseroRepository, BaseUserRepository baseUserRepository,
                         AuthorizationUtils authorizationUtils , ModelMapper modelMapper,
                         PasswordEncoder passwordEncoder, PedidoLocalService pedidoLocalService,
                         ApplicationEventPublisher eventPublisher) {
        this.meseroRepository = meseroRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.pedidoLocalService = pedidoLocalService;
        this.baseUserRepository = baseUserRepository;
        this.eventPublisher = eventPublisher;
    }

    public MeseroResponseDto findMeseroById(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + id + " no encontrado"));

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

        // Verifica si el email ya está registrado
        Optional<User> user = baseUserRepository.findByEmail(dto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo ya ha sido registrado");

        // Crear una nueva instancia de Mesero y asignar sus propiedades
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

        // Guardar el Mesero en el repositorio
        Mesero savedMesero = meseroRepository.save(mesero);

        // Publicar el evento BienvenidaMeseroEvent
        eventPublisher.publishEvent(new BienvenidaMeseroEvent(savedMesero, savedMesero.getEmail()));

        // Convertir el Mesero guardado a DTO y retornarlo
        return convertirADto(savedMesero);
    }

    public void deleteMesero(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + id + " no encontrado"));
        meseroRepository.delete(mesero);
    }

    public MeseroSelfResponseDto updateMesero(Long id, PatchMeseroDto dto) {
        // Buscar el mesero por su ID
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + id + " no encontrado"));

        // Map para registrar los campos que se actualizan
        Map<String, String> updatedFields = new HashMap<>();

        // Comparar y actualizar solo los campos proporcionados
        if (dto.getFirstName() != null && !dto.getFirstName().equals(mesero.getFirstName())) {
            updatedFields.put("Nombre", dto.getFirstName());
            mesero.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().equals(mesero.getLastName())) {
            updatedFields.put("Apellido", dto.getLastName());
            mesero.setLastName(dto.getLastName());
        }

        if (dto.getPassword() != null) {
            updatedFields.put("Contraseña", "Actualizada");
            mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPhone() != null && !dto.getPhone().equals(mesero.getPhoneNumber())) {
            updatedFields.put("Teléfono", dto.getPhone());
            mesero.setPhoneNumber(dto.getPhone());
        }

        // Actualizar la fecha de modificación
        mesero.setUpdatedAt(ZonedDateTime.now());

        // Guardar el mesero actualizado en el repositorio
        Mesero updatedMesero = meseroRepository.save(mesero);

        // Publicar evento con los campos actualizados
        eventPublisher.publishEvent(new PerfilUpdateMeseroEvent(updatedMesero, updatedFields, updatedMesero.getEmail()));

        // Convertir el mesero actualizado a DTO y retornarlo
        return modelMapper.map(updatedMesero, MeseroSelfResponseDto.class);
    }

    public List<PedidoLocalResponseDto> findMisPedidosLocalesActuales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con nombre de usuario " +username+ " no encontrado"));

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
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Mesero mesero = meseroRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + username + " no encontrado"));

        return modelMapper.map(mesero, MeseroSelfResponseDto.class);

    }

    public List<ReviewMeseroResponseDto> findMisReviews() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + username + " no encontrado"));

        List<ReviewMesero> reviewMesero = Optional.ofNullable(mesero.getReviewMeseros()).orElse(Collections.emptyList());

        return reviewMesero.stream()
                .map(review -> modelMapper.map(review, ReviewMeseroResponseDto.class)).toList();
    }

    public List<PedidoLocalResponseDto> findPedidosLocales() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con nombre de usuario " +username+ " no encontrado"));

        List<PedidoLocalResponseDto> pedidos = new ArrayList<>();
        for(PedidoLocal pedidoLocal : mesero.getPedidosLocales()){
                PedidoLocalResponseDto pedidoDto = pedidoLocalService.convertirADto(pedidoLocal);
                pedidos.add(pedidoDto);
        }
        return pedidos;
    }

    public MeseroSelfResponseDto updateAuthenticatedMesero(PatchMeseroDto dto) {
        // Obtener el email del usuario autenticado
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        // Buscar al mesero por su email
        Mesero mesero = meseroRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + username + " no encontrado"));

        // Registrar los campos actualizados
        Map<String, String> updatedFields = new HashMap<>();

        // Actualizar solo los campos que no sean nulos y sean diferentes
        if (dto.getFirstName() != null && !dto.getFirstName().equals(mesero.getFirstName())) {
            updatedFields.put("Nombre", dto.getFirstName());
            mesero.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null && !dto.getLastName().equals(mesero.getLastName())) {
            updatedFields.put("Apellido", dto.getLastName());
            mesero.setLastName(dto.getLastName());
        }

        if (dto.getPassword() != null) {
            updatedFields.put("Contraseña", "Actualizada");
            mesero.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPhone() != null && !dto.getPhone().equals(mesero.getPhoneNumber())) {
            updatedFields.put("Teléfono", dto.getPhone());
            mesero.setPhoneNumber(dto.getPhone());
        }

        // Actualizar la fecha de modificación
        mesero.setUpdatedAt(ZonedDateTime.now());

        // Guardar el mesero actualizado
        Mesero updatedMesero = meseroRepository.save(mesero);

        eventPublisher.publishEvent(new PerfilUpdateMeseroEvent(
                updatedMesero,
                updatedFields,
                updatedMesero.getEmail()
        ));

        return modelMapper.map(updatedMesero, MeseroSelfResponseDto.class);
    }

    // Métodos adicionales
    public void updateRatingScore(Long id) {
        Mesero mesero = meseroRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Mesero con " + id + " no encontrado"));

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
package com.example.proydbp.client.domain;

import com.example.proydbp.auth.utils.AuthorizationUtils;
import com.example.proydbp.client.dto.ClientRequestDto;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.events.email_event.BienvenidaClienteEvent;
import com.example.proydbp.events.email_event.PerfilUpdateClienteEvent;
import com.example.proydbp.exception.UnauthorizeOperationException;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.PedidoLocalService;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import com.example.proydbp.user.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    final private ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationUtils authorizationUtils;
    private final DeliveryService deliveryService;
    private final PedidoLocalService pedidoLocalService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ClientService(PedidoLocalService pedidoLocalService, AuthorizationUtils authorizationUtils,
                         DeliveryService deliveryService, ClientRepository clientRepository,
                         PasswordEncoder passwordEncoder, ModelMapper modelMapper,
                         ApplicationEventPublisher eventPublisher) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.deliveryService = deliveryService;
        this.pedidoLocalService = pedidoLocalService;
        this.eventPublisher = eventPublisher;
    }

    public ClientResponseDto getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        return convertirADto(client);
    }

    public Page<ClientSelfResponseDto> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clients = clientRepository.findAll(pageable);

        return clients.map(client -> modelMapper.map(client, ClientSelfResponseDto.class));
    }

    public ClientResponseDto saveClientDto(ClientRequestDto clientRequestDto) {
        // Comprobación si el cliente con el mismo email ya existe
        if (clientRepository.findByEmail(clientRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Cliente con email " + clientRequestDto.getEmail() + " ya existe.");
        }

        // Convertir el ClientRequestDto a Client
        Client client = modelMapper.map(clientRequestDto, Client.class);

        // Encriptar la contraseña y asignar otros datos básicos
        client.setPassword(passwordEncoder.encode(clientRequestDto.getPassword()));
        client.setRole(Role.CLIENT);
        client.setPhoneNumber(clientRequestDto.getPhone());
        client.setUpdatedAt(ZonedDateTime.now());
        client.setCreatedAt(ZonedDateTime.now());
        client.setRango(Rango.BRONZE);

        // Asegurarse de que las relaciones no sean nulas antes de guardar
        if (client.getPedidosLocales() == null) {
            client.setPedidosLocales(new ArrayList<>());
        }

        if (client.getDeliveries() == null) {
            client.setDeliveries(new ArrayList<>());
        }

        if (client.getReservations() == null) {
            client.setReservations(new ArrayList<>());
        }

        if (client.getReviewMeseros() == null) {
            client.setReviewMeseros(new ArrayList<>());
        }

        if (client.getReviewDeliveries() == null) {
            client.setReviewDeliveries(new ArrayList<>());
        }

        // Guardar el cliente
        clientRepository.save(client);

        // Publicar el evento de bienvenida
        BienvenidaClienteEvent bienvenidaClienteEvent = new BienvenidaClienteEvent(client, client.getEmail());
        eventPublisher.publishEvent(bienvenidaClienteEvent);

        // Convertir el cliente guardado a DTO y retornarlo
        return convertirADto(client);
    }

    public void deleteClient(Long id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        clientRepository.deleteById(id);
    }

    public ClientResponseDto updateClient(Long id, PatchClientDto patchClientDto) {
        // Buscar el cliente por su ID
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id " + id + " no encontrado."));

        // Map para registrar los campos que se actualizan
        Map<String, String> updatedFields = new HashMap<>();

        // Comparar y actualizar solo los campos proporcionados
        if (patchClientDto.getFirstName() != null && !patchClientDto.getFirstName().equals(client.getFirstName())) {
            updatedFields.put("Nombre", patchClientDto.getFirstName());
            client.setFirstName(patchClientDto.getFirstName());
        }

        if (patchClientDto.getLastName() != null && !patchClientDto.getLastName().equals(client.getLastName())) {
            updatedFields.put("Apellido", patchClientDto.getLastName());
            client.setLastName(patchClientDto.getLastName());
        }

        if (patchClientDto.getEmail() != null && !patchClientDto.getEmail().equals(client.getEmail())) {
            updatedFields.put("Correo", patchClientDto.getEmail());
            client.setEmail(patchClientDto.getEmail());
        }

        if (patchClientDto.getPhone() != null && !patchClientDto.getPhone().equals(client.getPhoneNumber())) {
            updatedFields.put("Teléfono", patchClientDto.getPhone());
            client.setPhoneNumber(patchClientDto.getPhone());
        }

        if (patchClientDto.getPassword() != null) {
            updatedFields.put("Contraseña", "Actualizada");
            client.setPassword(passwordEncoder.encode(patchClientDto.getPassword()));
        }

        // Actualizar la fecha de modificación
        client.setUpdatedAt(ZonedDateTime.now());

        // Guardar el cliente actualizado en el repositorio
        Client updatedClient = clientRepository.save(client);

        // Publicar evento con los campos actualizados
        eventPublisher.publishEvent(new PerfilUpdateClienteEvent(updatedClient, updatedFields, updatedClient.getEmail()));

        // Convertir y devolver el cliente actualizado como DTO
        return convertirADto(updatedClient);
    }


    public ClientSelfResponseDto getAuthenticatedClient(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username "+ username +" no encontrado."));

        return modelMapper.map(client, ClientSelfResponseDto.class);
    }

    public void deleteAuthenticatedClient(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username "+ username +" no encontrado."));

        clientRepository.deleteById(client.getId());
    }

    public ClientSelfResponseDto updateAuthenticatedClient(PatchClientDto patchClientDto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username "+ username +" no encontrado."));

        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setPassword(passwordEncoder.encode(patchClientDto.getPassword()));
        client.setPhoneNumber(patchClientDto.getPhone());
        client.setUpdatedAt(ZonedDateTime.now());
        clientRepository.save(client);
        return modelMapper.map(client, ClientSelfResponseDto.class);
    }

    public Page<PedidoLocalResponseDto> getAllPedidoLocal(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        // Crear objeto Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtener pedidos como una lista y convertir a DTO
        List<PedidoLocalResponseDto> pedidosDto = client.getPedidosLocales().stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());

        // Crear y devolver la página paginada
        return new PageImpl<>(
                pedidosDto.subList(
                        Math.min((int) pageable.getOffset(), pedidosDto.size()),
                        Math.min((int) pageable.getOffset() + pageable.getPageSize(), pedidosDto.size())
                ),
                pageable,
                pedidosDto.size()
        );
    }

    public Page<DeliveryResponseDto> getAllDelivery(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<DeliveryResponseDto> deliveryDtos = client.getDeliveries().stream()
                .map(deliveryService::convertirADto)
                .collect(Collectors.toList());

        return new PageImpl<>(
                deliveryDtos.subList(
                        Math.min((int) pageable.getOffset(), deliveryDtos.size()),
                        Math.min((int) pageable.getOffset() + pageable.getPageSize(), deliveryDtos.size())
                ),
                pageable,
                deliveryDtos.size()
        );
    }

    public Page<ReservationResponseDto> getAllReservation(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<ReservationResponseDto> reservationDtos = client.getReservations().stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .toList();

        return new PageImpl<>(
                reservationDtos.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                reservationDtos.size()
        );
    }


    public Void updateLoyaltyPoints(int point){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username "+ username +" no encontrado."));

        client.setLoyaltyPoints(point+client.getLoyaltyPoints());
        if (client.getLoyaltyPoints() < 100) {
            client.setRango(Rango.BRONZE);
        } else if (client.getLoyaltyPoints() < 500) {
            client.setRango(Rango.SILVER);
        } else if (client.getLoyaltyPoints() < 1000) {
            client.setRango(Rango.GOLD);
        } else {
            client.setRango(Rango.PLATINUM);
        }

        clientRepository.save(client);

        return null;
    }

    public Page<PedidoLocalResponseDto> getActualPedidoLocal(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<PedidoLocalResponseDto> filteredPedidos = client.getPedidosLocales().stream()
                .filter(pedido -> pedido.getStatus() == StatusPedidoLocal.EN_PREPARACION
                        || pedido.getStatus() == StatusPedidoLocal.LISTO
                        || pedido.getStatus() == StatusPedidoLocal.RECIBIDO)
                .map(pedido -> modelMapper.map(pedido, PedidoLocalResponseDto.class))
                .toList();

        return new PageImpl<>(
                filteredPedidos.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                filteredPedidos.size()
        );
    }

    public Page<DeliveryResponseDto> getActualDelivery(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<DeliveryResponseDto> filteredDeliveries = client.getDeliveries().stream()
                .filter(delivery -> delivery.getStatus() != StatusDelivery.ENTREGADO
                        && delivery.getStatus() != StatusDelivery.CANCELADO)
                .map(deliveryService::convertirADto)
                .toList();

        return new PageImpl<>(
                filteredDeliveries.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                filteredDeliveries.size()
        );
    }


    public Page<ReservationResponseDto> getActualReservation(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<ReservationResponseDto> filteredReservations = client.getReservations().stream()
                .filter(reservation -> reservation.getStatusReservation() != StatusReservation.FINALIZADA
                        && reservation.getStatusReservation() != StatusReservation.CANCELADO)
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .toList();

        return new PageImpl<>(
                filteredReservations.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                filteredReservations.size()
        );
    }

    public Page<ReviewMeseroResponseDto> getAllReviewMesero(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<ReviewMeseroResponseDto> reviewMeseros = client.getReviewMeseros().stream()
                .map(reviewMesero -> modelMapper.map(reviewMesero, ReviewMeseroResponseDto.class))
                .toList();

        return new PageImpl<>(
                reviewMeseros.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                reviewMeseros.size()
        );
    }


    public Page<ReviewDeliveryResponseDto> getAllReviewDelivery(int page, int size) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null) {
            throw new UnauthorizeOperationException("Usuario anónimo no tiene permitido acceder a este recurso");
        }

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con username " + username + " no encontrado."));

        Pageable pageable = PageRequest.of(page, size);

        List<ReviewDeliveryResponseDto> reviewDeliveries = client.getReviewDeliveries().stream()
                .map(reviewDelivery -> modelMapper.map(reviewDelivery, ReviewDeliveryResponseDto.class))
                .toList();

        return new PageImpl<>(
                reviewDeliveries.stream()
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .toList(),
                pageable,
                reviewDeliveries.size()
        );
    }



    public ClientResponseDto convertirADto(Client client) {

            ClientResponseDto clientDto = modelMapper.map(client, ClientResponseDto.class);

            List<DeliveryResponseDto> deliveriesResponse = new ArrayList<>();
            for (Delivery delivery : client.getDeliveries()) {
                deliveriesResponse.add(deliveryService.convertirADto(delivery));
            }
            clientDto.setDeliveries(deliveriesResponse);

            List<PedidoLocalResponseDto> pedidosResponse = new ArrayList<>();
            for (PedidoLocal pedido : client.getPedidosLocales()) {
            pedidosResponse.add(pedidoLocalService.convertirADto(pedido));
             }
            clientDto.setPedidosLocales(pedidosResponse);

        return clientDto;
    }
}

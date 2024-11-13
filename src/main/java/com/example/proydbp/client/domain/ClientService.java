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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    final private ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationUtils authorizationUtils;
    private final DeliveryService deliveryService;
    private final PedidoLocalService pedidoLocalService;


    @Autowired
    public ClientService(PedidoLocalService pedidoLocalService ,AuthorizationUtils authorizationUtils,DeliveryService deliveryService , ClientRepository clientRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorizationUtils = authorizationUtils;
        this.deliveryService = deliveryService;
        this.pedidoLocalService = pedidoLocalService;
    }


    public ClientResponseDto getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        return convertirADto(client);
    }

    public List<ClientResponseDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponseDto> clientsResponse = new ArrayList<>();

        for (Client client : clients) {
            ClientResponseDto clientDto = convertirADto(client);
            clientsResponse.add(clientDto);
        }

        return clientsResponse;
    }



    public ClientResponseDto saveClientDto(ClientRequestDto clientRequestDto) {
        if (clientRepository.findByEmail(clientRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Cliente con email " + clientRequestDto.getEmail() + " ya existe.");
        }
        Client client = modelMapper.map(clientRequestDto, Client.class);
        client.setPassword(passwordEncoder.encode(clientRequestDto.getPassword()));
        client.setRole(Role.CLIENT);
        client.setPhoneNumber(clientRequestDto.getPhone());
        client.setUpdatedAt(ZonedDateTime.now());
        client.setCreatedAt(ZonedDateTime.now());
        client.setRango(Rango.BRONZE);
        clientRepository.save(client);

        return convertirADto(client);
    }

    public void deleteClient(Long id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        clientRepository.deleteById(id);
    }

    public ClientResponseDto updateClient(Long id, PatchClientDto patchClientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));

        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setPassword(passwordEncoder.encode(patchClientDto.getPassword()));
        client.setPhoneNumber(patchClientDto.getPhone());
        client.setUpdatedAt(ZonedDateTime.now());
        clientRepository.save(client);
        return convertirADto(client);
    }

    public ClientSelfResponseDto getAuthenticatedClient(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado " + username));

        return modelMapper.map(client, ClientSelfResponseDto.class);
    }


    public void deleteAuthenticatedClient(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        clientRepository.deleteById(client.getId());
    }

    public ClientSelfResponseDto updateAuthenticatedClient(PatchClientDto patchClientDto) {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");
        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setPassword(passwordEncoder.encode(patchClientDto.getPassword()));
        client.setPhoneNumber(patchClientDto.getPhone());
        client.setUpdatedAt(ZonedDateTime.now());
        clientRepository.save(client);
        return modelMapper.map(client, ClientSelfResponseDto.class);
    }


    public List<PedidoLocalResponseDto> getAllPedidoLocal(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getPedidosLocales()
                .stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getAllDelivery(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));


        List<DeliveryResponseDto> deliverys = new ArrayList<>();

        for (Delivery delivery : client.getDeliveries()) {
            DeliveryResponseDto deliveryDto = deliveryService.convertirADto(delivery);
            deliverys.add(deliveryDto);
        }
        return deliverys;
    }

    public List<ReservationResponseDto> getAllReservation(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getReservations()
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    public Void updateLoyaltyPoints(int point){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

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



    public List<PedidoLocalResponseDto> getActualPedidoLocal(){

        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));


        return client.getPedidosLocales().stream()
                .filter(pedidoLocal -> pedidoLocal.getStatus() == StatusPedidoLocal.EN_PREPARACION || pedidoLocal.getStatus() == StatusPedidoLocal.LISTO || pedidoLocal.getStatus() == StatusPedidoLocal.RECIBIDO)  // Filtrar por estado enum
                .map(delivery -> modelMapper.map(delivery, PedidoLocalResponseDto.class))  // Mapear usando ModelMapper
                .collect(Collectors.toList());
    }


    public List<DeliveryResponseDto> getActualDelivery(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        List<DeliveryResponseDto> deliverys = new ArrayList<>();
        for(Delivery delivery : client.getDeliveries()){
            if(delivery.getStatus() != StatusDelivery.ENTREGADO && delivery.getStatus() != StatusDelivery.CANCELADO){
                DeliveryResponseDto deliveryDto = deliveryService.convertirADto(delivery);
                deliverys.add(deliveryDto);
            }

        }
        return deliverys;

    }

    public List<ReservationResponseDto> getActualReservation(){
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));


        return client.getReservations().stream()
                .filter(reservation -> reservation.getStatusReservation() != StatusReservation.FINALIZADA && reservation.getStatusReservation() != StatusReservation.CANCELADO)  // Filtrar por estado enum
                .map(delivery -> modelMapper.map(delivery, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ReviewMeseroResponseDto> getAllReviewMesero() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getReviewMeseros()
                .stream()
                .map(reviewMesero -> modelMapper.map(reviewMesero, ReviewMeseroResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ReviewDeliveryResponseDto> getAllReviewDelivery() {
        String username = authorizationUtils.getCurrentUserEmail();
        if (username == null)
            throw new UnauthorizeOperationException("Anonymous User not allowed to access this resource");

        Client client = clientRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getReviewDeliveries()
                .stream()
                .map(reviewDelivery -> modelMapper.map(reviewDelivery, ReviewDeliveryResponseDto.class))
                .collect(Collectors.toList());
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

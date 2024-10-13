package com.example.proydbp.client.domain;

import com.example.proydbp.client.dto.ClientRequestDto;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;

import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.domain.StatusReservation;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.user.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    final private ClientRepository clientRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.modelMapper = new ModelMapper();
    }

    public ClientResponseDto getClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        return modelMapper.map(client, ClientResponseDto.class);
    }

    public List<ClientResponseDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> modelMapper.map(client, ClientResponseDto.class))
                .collect(Collectors.toList());
    }


    public boolean clientExists(ClientRequestDto clientRequestDto) {
        Optional<Client> existingClient = clientRepository.findByEmail(clientRequestDto.getEmail());
        return existingClient.isPresent();
    }

    public boolean clientExists(PatchClientDto patchClientDto) {
        Optional<Client> existingClient = clientRepository.findByEmail(patchClientDto.getEmail());
        return existingClient.isPresent();
    }


    public ClientResponseDto saveClientDto(ClientRequestDto clientRequestDto) {
        Client client = modelMapper.map(clientRequestDto, Client.class);
        client.setRole(Role.CLIENT);
        clientRepository.save(client);
        return modelMapper.map(client, ClientResponseDto.class);
    }

    public String getIdClient(ClientRequestDto clientRequestDto){
        Client client = clientRepository.findByEmail(clientRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con email " + clientRequestDto.getEmail() + " no encontrado."));
        return String.valueOf(client.getId());
    }

    public boolean clientExists(Long id) {
        return clientRepository.existsById(id);
    }

    public void deleteClient(Long id) {
        clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
        clientRepository.deleteById(id);
    }

    public void updateClient(Long id, PatchClientDto patchClientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));


        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setEmail(patchClientDto.getEmail());
        client.setPassword(patchClientDto.getPassword());
        client.setPhoneNumber(patchClientDto.getPhoneNumber());
        clientRepository.save(client);
    }

    public ClientResponseDto getAuthenticatedClient(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return modelMapper.map(client, ClientResponseDto.class);

    }


    public void deleteAuthenticatedClient(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        clientRepository.deleteById(client.getId());
    }

    public void updateAuthenticatedClient(PatchClientDto patchClientDto) {
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setEmail(patchClientDto.getEmail());
        client.setPassword(patchClientDto.getPassword());
        client.setPhoneNumber(patchClientDto.getPhoneNumber());
        clientRepository.save(client);
    }

    public List<PedidoLocalResponseDto> getAllPedidoLocal(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getPedidoLocales()
                .stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getAllDelivery(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getDeliveries()
                .stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> getAllReservation(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getReservations()
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    public Void updateLoyaltyPoints(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        if (client.getLoyaltyPoints() < 100) {
            client.setRango(Rango.BRONZE);
        } else if (client.getLoyaltyPoints() < 500) {
            client.setRango(Rango.SILVER);
        } else if (client.getLoyaltyPoints() < 1000) {
            client.setRango(Rango.GOLD);
        } else {
            client.setRango(Rango.PLATINUM);
        }

        // Incluir el evento
        return null;
    }


    // adicional

    public List<PedidoLocalResponseDto> getActualPedidoLocal(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getPedidoLocales().stream()
                .filter(pedidoLocal -> pedidoLocal.getStatus() == StatusPedidoLocal.ENTREGADO || pedidoLocal.getStatus() == StatusPedidoLocal.CANCELADO)  // Filtrar por estado enum
                .map(delivery -> modelMapper.map(delivery, PedidoLocalResponseDto.class))  // Mapear usando ModelMapper
                .collect(Collectors.toList());  // Recoger la lista filtrada
    }

    public List<DeliveryResponseDto> getActualDelivery(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        // Filtrar los deliveries que no están en estado "ENTREGADO" y mapear a DeliveryResponseDto usando ModelMapper

        return client.getDeliveries().stream()
                .filter(delivery -> delivery.getStatus() != StatusDelivery.ENTREGADO && delivery.getStatus() != StatusDelivery.CANCELADO)  // Filtrar por estado enum
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))  // Mapear usando ModelMapper
                .collect(Collectors.toList());  // Recoger la lista filtrada
    }

    public List<ReservationResponseDto> getActualReservation(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return client.getReservations().stream()
                .filter(reservation -> reservation.getStatusReservation() != StatusReservation.CONFIRMADO && reservation.getStatusReservation() != StatusReservation.CANCELADO)  // Filtrar por estado enum
                .map(delivery -> modelMapper.map(delivery, ReservationResponseDto.class))  // Mapear usando ModelMapper
                .collect(Collectors.toList());  // Recoger la lista filtrada
    }
}

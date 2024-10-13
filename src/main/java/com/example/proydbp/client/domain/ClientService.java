package com.example.proydbp.client.domain;

import com.example.proydbp.client.dto.ClientRequestDto;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;

import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.dto.ReservationDto;
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
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id "+ id +"no encontrado."));
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
        clientRepository.save(client);
        return modelMapper.map(client, ClientResponseDto.class);
    }

    public String getIdClient(ClientRequestDto clientRequestDto){
        Client client = clientRepository.findByEmail(clientRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con email " + clientRequestDto.getEmail() + " no encontrado."));
        return String.valueOf(client.getId());
    }

    public boolean clientExists(Long id) {
        return clientRepository.existsById(id);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(Long id, PatchClientDto patchClientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con id "+ id +"no encontrado."));


        client.setFirstName(patchClientDto.getFirstName());
        client.setLastName(patchClientDto.getLastName());
        client.setEmail(patchClientDto.getEmail());
        client.setPassword(patchClientDto.getPassword());
        client.setPhoneNumber(patchClientDto.getPhoneNumber());
        client.setAddress(patchClientDto.getAddress());
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
        client.setAddress(patchClientDto.getAddress());
        clientRepository.save(client);
    }

    public List<PedidoLocalResponseDto> getActualPedidoLocal(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getPedidoLocal()
                .stream()
                .map(pedidoLocal -> modelMapper.map(pedidoLocal, PedidoLocalResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<DeliveryResponseDto> getActualDelivery(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getDelivery()
                .stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<ReservationDto> getActualReservation(){
        String clientName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository
                .findByEmail(clientName)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));
        return client.getReservation()
                .stream()
                .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
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
        System.out.println(String.format("Felicidades, tu rango actual es: %s", client.getRango()));
        return null;
    }

}

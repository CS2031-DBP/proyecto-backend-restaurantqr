package com.example.proydbp.client.domain;

import com.example.proydbp.client.dto.ClientDto;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.user.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    final private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public boolean clientExists(ClientDto clientDto) {
        Optional<Client> existingClient = clientRepository.findByEmail(clientDto.getEmail());
        return existingClient.isPresent();
    }

    public boolean clientExists(Long id) {
        return clientRepository.existsById(id);
    }

    public String saveClientDto(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPassword(clientDto.getPassword());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        client.setRole(Role.CLIENT);
        clientRepository.save(client);

        return clientDto.toString();
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(Long id, ClientDto clientDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPassword(clientDto.getPassword());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        clientRepository.save(client);
    }

    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente con id "+ id +"no encontrado."));
    }
}

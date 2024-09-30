package com.example.proydbp.client.application;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.ClientService;
import com.example.proydbp.client.dto.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    final private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> saveClientDto(@RequestBody ClientDto clientDto) {
        if(clientService.clientExists(clientDto)){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        String uri = clientService.saveClientDto(clientDto);
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @GetMapping("/{id}")
    ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clients);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientService.clientExists(id)) {
            return ResponseEntity.notFound().build();
        }
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        if (!clientService.clientExists(id)) {
            return ResponseEntity.notFound().build();
        }
        clientService.updateClient(id, clientDto);
        return ResponseEntity.noContent().build();
    }
}

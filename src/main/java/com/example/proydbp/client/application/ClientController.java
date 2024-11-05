package com.example.proydbp.client.application;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.ClientService;
import com.example.proydbp.client.dto.ClientRequestDto;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> findClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientResponseDto>> findAllClients() {
        List<ClientResponseDto> clientResponseDtos = clientService.getAllClients();
        if (clientResponseDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(clientResponseDtos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Client> createClient(@RequestBody ClientRequestDto clientRequestDto) {
        if (clientService.clientExists(clientRequestDto)) {
            throw new UserAlreadyExistException("Cliente con email " + clientRequestDto.getEmail() + " ya existe.");
        }
        clientService.saveClientDto(clientRequestDto);

        String uri = clientService.getIdClient(clientRequestDto);
        return ResponseEntity.created(URI.create(uri)).build();
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateClient(@PathVariable Long id, @RequestBody PatchClientDto patchClientDto) {
        clientService.updateClient(id, patchClientDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> getAuthenticatedClient() {
        return ResponseEntity.ok(clientService.getAuthenticatedClient());

    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAuthenticatedClient() {
        clientService.deleteAuthenticatedClient();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> updateAuthenticatedClient(@RequestBody PatchClientDto patchClientDto) {
        System.out.println("No entre");
        if (!clientService.clientExists(patchClientDto)) {
            return ResponseEntity.notFound().build();
        }
        clientService.updateAuthenticatedClient(patchClientDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me/pedidoLocal")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<PedidoLocalResponseDto>> getAllPedidoLocal() {
        return ResponseEntity.ok(clientService.getActualPedidoLocal());
    }

    @GetMapping("me/delivery")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<DeliveryResponseDto>> getAllDelivery() {
        return ResponseEntity.ok(clientService.getActualDelivery());
    }

    @GetMapping("me/reservation")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservation() {
        return ResponseEntity.ok(clientService.getActualReservation());
    }

    @GetMapping("/me/pedidoLocalActual")
    @PreAuthorize("hasRole('CLIENT')")
    public List<PedidoLocalResponseDto> getActualPedidoLocal() {
        return clientService.getAllPedidoLocal();
    }

    @GetMapping("/me/deliveryActual")
    @PreAuthorize("hasRole('CLIENT')")
    public List<DeliveryResponseDto> getActualDelivery() {
        return clientService.getAllDelivery();
    }

    @GetMapping("/me/reservationActual")
    @PreAuthorize("hasRole('CLIENT')")
    public List<ReservationResponseDto> getActualReservation() {
        return clientService.getAllReservation();
    }
}

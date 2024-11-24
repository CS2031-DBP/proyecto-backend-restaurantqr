package com.example.proydbp.client.application;

import com.example.proydbp.client.domain.ClientService;
import com.example.proydbp.client.dto.ClientRequestDto;
import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.ClientSelfResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/client")
public class ClientController {

    final private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientResponseDto> findClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody ClientRequestDto clientRequestDto) {
        return  ResponseEntity.status(201).body(clientService.saveClientDto(clientRequestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable Long id, @RequestBody PatchClientDto patchClientDto) {
        return ResponseEntity.ok(clientService.updateClient(id, patchClientDto));
    }



    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ClientSelfResponseDto> getAuthenticatedClient() {
        return ResponseEntity.ok(clientService.getAuthenticatedClient());

    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Void> deleteAuthenticatedClient() {
        clientService.deleteAuthenticatedClient();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ClientSelfResponseDto> updateAuthenticatedClient(@RequestBody PatchClientDto patchClientDto) {
        return ResponseEntity.ok(clientService.updateAuthenticatedClient(patchClientDto));
    }




    //Paginación

    @GetMapping("me/pedidoLocalActual")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<PedidoLocalResponseDto>> getActualPedidoLocal(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(clientService.getActualPedidoLocal(page,size));
    }

    @GetMapping("me/deliveryActual")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<DeliveryResponseDto>> getActualDelivery(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(clientService.getActualDelivery(page,size));
    }

    @GetMapping("me/reservationActual")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Page<ReservationResponseDto>> getActualReservation(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(clientService.getActualReservation(page,size));
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<ClientSelfResponseDto>> getAllClients(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(clientService.getAllClients(page,size));
    }

    @GetMapping("/me/pedidoLocal")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<PedidoLocalResponseDto> getAllPedidoLocal(@RequestParam int page, @RequestParam int size) {
        return clientService.getAllPedidoLocal(page,size);
    }

    @GetMapping("/me/delivery")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<DeliveryResponseDto> getAllDelivery(@RequestParam int page, @RequestParam int size) {
        return clientService.getAllDelivery(page,size);
    }

    @GetMapping("/me/reservation")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<ReservationResponseDto> getAllReservation(@RequestParam int page, @RequestParam int size) {
        return clientService.getAllReservation(page,size);
    }

    @GetMapping("/me/reviewMesero")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<ReviewMeseroResponseDto> getAllReviewMesero(@RequestParam int page, @RequestParam int size) {
        return clientService.getAllReviewMesero(page,size);
    }

    @GetMapping("/me/reviewDelivery")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Page<ReviewDeliveryResponseDto> getAllReviewDelivery(@RequestParam int page, @RequestParam int size) {
        return clientService.getAllReviewDelivery(page,size);
    }
}

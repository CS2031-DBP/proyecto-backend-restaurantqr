package com.example.proydbp.delivery.application;

import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    final private DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Acceso para administradores
    public ResponseEntity<DeliveryResponseDto> findDeliveryById(@PathVariable Long id) {
        DeliveryResponseDto delivery = deliveryService.findDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Solo acceso para administradores
    public ResponseEntity<List<DeliveryResponseDto>> findAllDeliveries() {
        List<DeliveryResponseDto> deliveries = deliveryService.findAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')") // Acceso para usuarios
    public ResponseEntity<DeliveryResponseDto> createDelivery(@RequestBody DeliveryRequestDto dto) {
        DeliveryResponseDto deliveryResponse = deliveryService.createDelivery(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Solo acceso para administradores
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')") // Solo acceso para usuarios
    public ResponseEntity<DeliveryResponseDto> updateDelivery(@PathVariable Long id, @RequestBody DeliveryRequestDto dto) {
        DeliveryResponseDto deliveryResponse = deliveryService.updateDelivery(id, dto);
        return ResponseEntity.ok(deliveryResponse);
    }

    @PatchMapping("/{id}/entregado")
    @PreAuthorize("hasRole('REPARTIDOR')") // Acceso para repartidores
    public ResponseEntity<DeliveryResponseDto> entregadoDelivery(@PathVariable Long id) {
        DeliveryResponseDto deliveryResponse = deliveryService.entregadoDelivery(id);
        return ResponseEntity.ok(deliveryResponse);
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ADMIN')") // Acceso para administradores
    public ResponseEntity<List<DeliveryResponseDto>> findCurrentDeliveries() {
        List<DeliveryResponseDto> currentDeliveries = deliveryService.findCurrentDeliveries();
        return ResponseEntity.ok(currentDeliveries);
    }

    @PutMapping("/{id}/listo")
    @PreAuthorize("hasRole('CHEF')") // Acceso para chefs
    public ResponseEntity<DeliveryResponseDto> listoDelivery(@PathVariable Long id) {
        DeliveryResponseDto deliveryResponse = deliveryService.listoDelivery(id);
        return ResponseEntity.ok(deliveryResponse);
    }

    @PatchMapping("/{id}/en-preparacion")
    @PreAuthorize("hasRole('CHEF')") // Acceso para chefs
    public ResponseEntity<DeliveryResponseDto> enPreparacionDelivery(@PathVariable Long id) {
        DeliveryResponseDto deliveryResponse = deliveryService.enPreparacionDelivery(id);
        return ResponseEntity.ok(deliveryResponse);
    }

    @PatchMapping("/{id}/cancelado")
    @PreAuthorize("hasRole('CLIENT')") // Acceso para usuarios
    public ResponseEntity<DeliveryResponseDto> canceladoDelivery(@PathVariable Long id) {
        DeliveryResponseDto deliveryResponse = deliveryService.canceladoDelivery(id);
        return ResponseEntity.ok(deliveryResponse);
    }

    @PatchMapping("/{id}/enCamino")
    @PreAuthorize("hasRole('REPARTIDOR')") // Acceso para repartidores
    public ResponseEntity<DeliveryResponseDto> enCaminoDelivery(@PathVariable Long id) {
        DeliveryResponseDto deliveryResponse = deliveryService.enCaminoDelivery(id);
        return ResponseEntity.ok(deliveryResponse);
    }

}

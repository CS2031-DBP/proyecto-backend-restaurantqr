package com.example.proydbp.delivery.application;

import com.example.proydbp.delivery.domain.DeliveryService;
import com.example.proydbp.delivery.dto.DeliveryDto;
import com.example.proydbp.delivery.dto.DeliveryRequestDto;
import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.delivery.dto.PatchDeliveryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> getDeliveryById(@PathVariable Long id) {
        DeliveryResponseDto delivery = deliveryService.findDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<DeliveryResponseDto>> getAllDeliveries() {
        List<DeliveryResponseDto> deliveries = deliveryService.findAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/")
    public ResponseEntity<DeliveryResponseDto> createDelivery(@RequestBody DeliveryRequestDto dto) {
        DeliveryResponseDto createdDelivery = deliveryService.createDelivery(dto);
        return ResponseEntity.ok(createdDelivery);
    }

    @PreAuthorize("hasRole('CLIENT') or hasRole('REPARTIDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }







    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<DeliveryResponseDto> updateDelivery(@PathVariable Long id, @RequestBody PatchDeliveryDto dto) {
        DeliveryResponseDto updatedDelivery = deliveryService.updateDelivery(id, dto);
        return ResponseEntity.ok(updatedDelivery);
    }

    @PreAuthorize("hasRole('CHEF')")
    @PatchMapping("/cocinando/{id}")
    public ResponseEntity<Void> preparingDelivery(@PathVariable Long id) {
        deliveryService.endDeliveryPreparando(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CHEF')")
    @PatchMapping("/listo/{id}")
    public ResponseEntity<Void> endDelivery(@PathVariable Long id) {
        deliveryService.endDeliveryListo(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CHEF')")
    @GetMapping("/deliveriesRecibidos")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliveriesRecibidos() {
        List<DeliveryResponseDto> deliveries = deliveryService.findDeliveriesRecibidos();
        return ResponseEntity.ok(deliveries);
    }

}

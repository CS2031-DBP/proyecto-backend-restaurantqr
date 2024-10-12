package com.example.proydbp.repartidor.application;

import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.domain.RepartidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repartidor")
public class RepartidorController {

    private final RepartidorService repartidorService;

    @Autowired
    public RepartidorController(RepartidorService repartidorService) {
        this.repartidorService = repartidorService;
    }


    @GetMapping("/{id}")

    public ResponseEntity<RepartidorResponseDto> getRepartidorById(@PathVariable Long id) {
        RepartidorResponseDto repartidor = repartidorService.findRepartidorById(id);
        return ResponseEntity.ok(repartidor);
    }


    @GetMapping

    public ResponseEntity<List<RepartidorResponseDto>> getAllRepartidores() {
        List<RepartidorResponseDto> repartidores = repartidorService.findAllRepartidors();
        return ResponseEntity.ok(repartidores);
    }


    @PostMapping

    public ResponseEntity<RepartidorResponseDto> createRepartidor(@RequestBody RepartidorRequestDto requestDto) {
        RepartidorResponseDto createdRepartidor = repartidorService.createRepartidor(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRepartidor);
    }


    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteRepartidor(@PathVariable Long id) {
        repartidorService.deleteRepartidor(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}")

    public ResponseEntity<RepartidorResponseDto> updateRepartidor(@PathVariable Long id, @RequestBody PatchRepartidorDto patchDto) {
        RepartidorResponseDto updatedRepartidor = repartidorService.updateRepartidor(id, patchDto);
        return ResponseEntity.ok(updatedRepartidor);
    }


    @GetMapping("/me")

    public ResponseEntity<RepartidorResponseDto> getAuthenticatedRepartidor(Long id) {
        RepartidorResponseDto repartidor = repartidorService.findAuthenticatedRepartidor(id);
        return ResponseEntity.ok(repartidor);
    }


    @GetMapping("/me/deliverysActuales")

    public ResponseEntity<List<DeliveryResponseDto>> getDeliverysActuales( Long id) {
        List<DeliveryResponseDto> deliverys = repartidorService.findDeliverysActuales(id);
        return ResponseEntity.ok(deliverys);
    }


    @PatchMapping("/me/{idDelivery}/encamino")

    public ResponseEntity<Void> enCaminoDelivery(@PathVariable Long idDelivery) {
        repartidorService.enCaminoDelivery(idDelivery);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/{idDelivery}/entregado")
    public ResponseEntity<Void> endDelivery(@PathVariable Long idDelivery) {
        repartidorService.endDelivery(idDelivery);
        return ResponseEntity.noContent().build();
    }
}
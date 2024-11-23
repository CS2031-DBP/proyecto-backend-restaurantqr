package com.example.proydbp.repartidor.application;

import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.domain.RepartidorService;
import com.example.proydbp.repartidor.dto.RepartidorSelfResponseDto;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repartidor")
public class RepartidorController {

    private final RepartidorService repartidorService;
    private final RepartidorRepository repartidorRepository;

    @Autowired
    public RepartidorController(RepartidorService repartidorService,
                                RepartidorRepository repartidorRepository) {
        this.repartidorService = repartidorService;
        this.repartidorRepository = repartidorRepository;
    }

    //CRUD ADMIN

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RepartidorResponseDto> getRepartidorById(@PathVariable Long id) {
        return ResponseEntity.ok(repartidorService.findRepartidorById(id));
    }

    @PreAuthorize("hasRole('ROLE_REPARTIDOR')")
    @PatchMapping("/me")
    public ResponseEntity<RepartidorSelfResponseDto> updateRepartidorAuthenticado(@RequestBody PatchRepartidorDto patchDto) {
        return ResponseEntity.ok(repartidorService.updateAuthenticatedRepartidor(patchDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<RepartidorResponseDto> createRepartidor(@RequestBody RepartidorRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repartidorService.createRepartidor(requestDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepartidor(@PathVariable Long id) {
        repartidorService.deleteRepartidor(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<RepartidorSelfResponseDto> updateRepartidor(@PathVariable Long id, @RequestBody PatchRepartidorDto patchDto) {
        return ResponseEntity.ok(repartidorService.updateRepartidor(id, patchDto));
    }

    //

    @PreAuthorize("hasRole('ROLE_REPARTIDOR')")
    @GetMapping("/me")
    public ResponseEntity<RepartidorSelfResponseDto> getAuthenticatedRepartidor() {
        return ResponseEntity.ok(repartidorService.findAuthenticatedRepartidor());
    }


    //Paginacion
    @PreAuthorize("hasRole('ROLE_REPARTIDOR')")
    @GetMapping("/me/deliveriesActuales")
    public ResponseEntity<Page<DeliveryResponseDto>> getDeliverysActuales(@RequestParam int page, @RequestParam int size) {
        Page<DeliveryResponseDto> deliveriesActuales = repartidorService.findDeliverysActuales(page, size);
        return ResponseEntity.ok(deliveriesActuales);
    }

    @PreAuthorize("hasRole('ROLE_REPARTIDOR')")
    @GetMapping("/me/misDeliveries")
    public ResponseEntity<Page<DeliveryResponseDto>> getDeliverys(@RequestParam int page, @RequestParam int size) {
        Page<DeliveryResponseDto> deliveries = repartidorService.findDeliverys(page, size);
        return ResponseEntity.ok(deliveries);
    }

    @PreAuthorize("hasRole('ROLE_REPARTIDOR')")
    @GetMapping("/me/misReviews")
    public ResponseEntity<Page<ReviewDeliveryResponseDto>> findMisReviews(@RequestParam int page, @RequestParam int size) {
        Page<ReviewDeliveryResponseDto> reviews = repartidorService.findMisReviews(page, size);
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<RepartidorResponseDto>> getAllRepartidores(@RequestParam int page, @RequestParam int size) {
        Page<RepartidorResponseDto> repartidores = repartidorService.findAllRepartidors(page, size);
        return ResponseEntity.ok(repartidores);
    }

}
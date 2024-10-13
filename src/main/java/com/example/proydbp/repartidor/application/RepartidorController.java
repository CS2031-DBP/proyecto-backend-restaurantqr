package com.example.proydbp.repartidor.application;

import com.example.proydbp.delivery.dto.DeliveryResponseDto;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.dto.PatchRepartidorDto;
import com.example.proydbp.repartidor.dto.RepartidorRequestDto;
import com.example.proydbp.repartidor.dto.RepartidorResponseDto;
import com.example.proydbp.repartidor.domain.RepartidorService;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.reviewDelivery.domain.ReviewDelivery;
import com.example.proydbp.reviewDelivery.dto.ReviewDeliveryResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RepartidorResponseDto> getRepartidorById(@PathVariable Long id) {
        RepartidorResponseDto repartidor = repartidorService.findRepartidorById(id);
        return ResponseEntity.ok(repartidor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RepartidorResponseDto>> getAllRepartidores() {
        List<RepartidorResponseDto> repartidores = repartidorService.findAllRepartidors();
        return ResponseEntity.ok(repartidores);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RepartidorResponseDto> createRepartidor(@RequestBody RepartidorRequestDto requestDto) {
        RepartidorResponseDto createdRepartidor = repartidorService.createRepartidor(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRepartidor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepartidor(@PathVariable Long id) {
        repartidorService.deleteRepartidor(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<RepartidorResponseDto> updateRepartidor(@PathVariable Long id, @RequestBody PatchRepartidorDto patchDto) {
        RepartidorResponseDto updatedRepartidor = repartidorService.updateRepartidor(id, patchDto);
        return ResponseEntity.ok(updatedRepartidor);
    }

    @PreAuthorize("hasRole('REPARTIDOR')")
    @GetMapping("/me")
    public ResponseEntity<RepartidorResponseDto> getAuthenticatedRepartidor(Long id) {
        RepartidorResponseDto repartidor = repartidorService.findAuthenticatedRepartidor(id);
        return ResponseEntity.ok(repartidor);
    }

    @PreAuthorize("hasRole('REPARTIDOR')")
    @GetMapping("/me/deliverysActuales")
    public ResponseEntity<List<DeliveryResponseDto>> getDeliverysActuales() {
        List<DeliveryResponseDto> deliverys = repartidorService.findDeliverysActuales();
        return ResponseEntity.ok(deliverys);
    }


    @PreAuthorize("hasRole('REPARTIDOR')")
    @GetMapping("/me/misreviews")
    public ResponseEntity<List<ReviewDeliveryResponseDto>> findMisReviews() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Repartidor repartidor = repartidorRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Repartidor no encontrado"));

        List<ReviewDeliveryResponseDto> reviews = repartidorService.findMisReviews(repartidor.getId());
        return ResponseEntity.ok(reviews);
    }

}
package com.example.proydbp.mesero.application;

import com.example.proydbp.client.dto.ClientResponseDto;
import com.example.proydbp.client.dto.PatchClientDto;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.domain.MeseroService;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.reviewMesero.dto.ReviewMeseroResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mesero")
public class MeseroController {

    final private MeseroService meseroService;
    final private MeseroRepository meseroRepository;

    @Autowired
    public MeseroController(MeseroService meseroService, MeseroRepository meseroRepository) {
        this.meseroService = meseroService;
        this.meseroRepository = meseroRepository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MeseroResponseDto> getMeseroById(@PathVariable Long id) {
        return ResponseEntity.ok(meseroService.findMeseroById(id));
    }



    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MeseroResponseDto> createMesero(@RequestBody @Valid MeseroRequestDto dto) {
        return new ResponseEntity<>(meseroService.createMesero(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesero(@PathVariable Long id) {
        meseroService.deleteMesero(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<MeseroSelfResponseDto> updateMesero(@PathVariable Long id, @RequestBody PatchMeseroDto dto) {
        return ResponseEntity.ok(meseroService.updateMesero(id, dto));
    }




    @PreAuthorize("hasRole('ROLE_MESERO')")
    @GetMapping("/me")
    public ResponseEntity<MeseroSelfResponseDto> findMesero() {
        return ResponseEntity.ok(meseroService.getMeseroOwnInfo());
    }
    @PatchMapping("/me")
    @PreAuthorize("hasRole('ROLE_MESERO')")
    public ResponseEntity<MeseroSelfResponseDto> updateAuthenticatedClient(@RequestBody PatchMeseroDto dto) {
        return ResponseEntity.ok(meseroService.updateAuthenticatedMesero(dto));
    }


    //PAginación



    @PreAuthorize("hasRole('ROLE_MESERO')")
    @GetMapping("/me/pedidosLocalesActuales")
    public ResponseEntity<List<PedidoLocalResponseDto>> findMisPedidosLocalesActuales() {
        return ResponseEntity.ok(meseroService.findMisPedidosLocalesActuales());
    }


    @PreAuthorize("hasRole('ROLE_MESERO')")
    @GetMapping("/me/misReviews")
    public ResponseEntity<List<ReviewMeseroResponseDto>> findMisReviews() {
        return ResponseEntity.ok(meseroService.findMisReviews());
    }


    @PreAuthorize("hasRole('ROLE_MESERO')")
    @GetMapping("/me/pedidosLocales")
    public ResponseEntity<List<PedidoLocalResponseDto>> findPedidosLocales() {
        return ResponseEntity.ok(meseroService.findPedidosLocales());
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<MeseroResponseDto>> findAllMeseros() {
        return ResponseEntity.ok(meseroService.findAllMeseros());
    }

}

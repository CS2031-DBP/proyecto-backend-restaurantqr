package com.example.proydbp.mesero.application;

import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.domain.MeseroService;
import com.example.proydbp.mesero.dto.MeseroRequestDto;
import com.example.proydbp.mesero.dto.MeseroResponseDto;
import com.example.proydbp.mesero.dto.MeseroSelfResponseDto;
import com.example.proydbp.mesero.dto.PatchMeseroDto;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import com.example.proydbp.pedido_local.infrastructure.PedidoLocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class MeseroController {

    final private MeseroService meseroService;

    @Autowired
    public MeseroController(MeseroService meseroService) {
        this.meseroService = meseroService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeseroResponseDto> getMeseroById(@PathVariable Long id) {
        return ResponseEntity.ok(meseroService.findMeseroById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<MeseroSelfResponseDto> findMesero() {
        return ResponseEntity.ok(meseroService.getMeseroOwnInfo());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeseroResponseDto>> findAllMeseros() {
        List<MeseroResponseDto> meseros = meseroService.findAllMeseros();
        return ResponseEntity.ok(meseros);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeseroResponseDto> createMesero(@RequestBody MeseroRequestDto dto) {
        MeseroResponseDto createdMesero = meseroService.createMesero(dto);
        return new ResponseEntity<>(createdMesero, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMesero(@PathVariable Long id) {
        meseroService.deleteMesero(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<MeseroResponseDto> updateMesero(@PathVariable Long id, @RequestBody PatchMeseroDto dto) {
        MeseroResponseDto updatedMesero = meseroService.updateMesero(id, dto);
        return ResponseEntity.ok(updatedMesero);
    }

    @PreAuthorize("hasRole('MESERO')")
    @GetMapping("/me/pedidosLocalesActuales")
    public ResponseEntity<List<PedidoLocalResponseDto>> findPedidosLocalesActuales(Long idMesero) {
        List<PedidoLocalResponseDto> pedidosLocales = meseroService.findPedidosLocalesActuales(idMesero);
        return ResponseEntity.ok(pedidosLocales);
    }

    @PreAuthorize("hasRole('MESERO')")
    @PatchMapping("/me/{idPedidoLocal}/encamino")
    public ResponseEntity<Void> pedidoLocalListo(@PathVariable Long idPedidoLocal) {
        meseroService.pedidoLocalListo(idPedidoLocal);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MESERO')")
    @PatchMapping("/me/{idPedidoLocal}/entregado")
    public ResponseEntity<Void> pedidoLocalEntregado(@PathVariable Long idPedidoLocal) {
        meseroService.pedidoLocalEntregado(idPedidoLocal);
        return ResponseEntity.noContent().build();
    }

    // adicionales

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/rating")
    public ResponseEntity<Void> updateRatingScore(@PathVariable Long id, @RequestBody PatchMeseroDto patchMeseroDto) {
        meseroService.updateRatingScore(patchMeseroDto, id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/asignar")
    public ResponseEntity<MeseroResponseDto> asignarMesero() {
        Mesero mesero = meseroService.asignarMesero();
        MeseroResponseDto meseroDto = new MeseroResponseDto();
        meseroDto.setId(mesero.getId());
        meseroDto.setFirstName(mesero.getFirstName());
        meseroDto.setLastName(mesero.getLastName());

        return ResponseEntity.ok(meseroDto);
    }
}

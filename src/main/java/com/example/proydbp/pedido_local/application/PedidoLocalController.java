package com.example.proydbp.pedido_local.application;

import com.example.proydbp.pedido_local.domain.PedidoLocalService;
import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidolocal")
public class PedidoLocalController {

    final private PedidoLocalService pedidoLocalService;

    @Autowired
    public PedidoLocalController(PedidoLocalService pedidoLocalService) {
        this.pedidoLocalService = pedidoLocalService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoLocalResponseDto> findPedidoLocalById(@PathVariable Long id) {
        PedidoLocalResponseDto pedidoLocal = pedidoLocalService.findPedidoLocalById(id);
        return ResponseEntity.ok(pedidoLocal);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoLocalResponseDto>> findAllPedidoLocals() {
        List<PedidoLocalResponseDto> pedidoLocals = pedidoLocalService.findAllPedidoLocals();
        return ResponseEntity.ok(pedidoLocals);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoLocalResponseDto> createPedidoLocal(@RequestBody PedidoLocalRequestDto dto) {
        PedidoLocalResponseDto createdPedidoLocal = pedidoLocalService.createPedidoLocal(dto);
        return new ResponseEntity<>(createdPedidoLocal, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePedidoLocal(@PathVariable Long id) {
        pedidoLocalService.deletePedidoLocal(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoLocalResponseDto> updatePedidoLocal(@PathVariable Long id, @RequestBody PatchPedidoLocalDto dto) {
        PedidoLocalResponseDto updatedPedidoLocal = pedidoLocalService.updatePedidoLocal(id, dto);
        return ResponseEntity.ok(updatedPedidoLocal);
    }

    @PatchMapping("/cocinando/{id}")
    @PreAuthorize("hasRole('CHEF')")
    public ResponseEntity<Void> cocinandoPedidoLocal(@PathVariable Long id) {
        pedidoLocalService.cocinandoPedidoLocal(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CHEF')")
    @PatchMapping("/listo/{id}")
    public ResponseEntity<Void> listoPedidoLocal(@PathVariable Long id) {
        pedidoLocalService.listoPedidoLocal(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CHEF')")
    @GetMapping("/pedidosLocalesRecibidos")
    public ResponseEntity<List<PedidoLocalResponseDto>> findPedidosLocalesRecibidos() {
        List<PedidoLocalResponseDto> pedidosRecibidos = pedidoLocalService.findPedidosLocalesRecibidos();
        return ResponseEntity.ok(pedidosRecibidos);
    }
}

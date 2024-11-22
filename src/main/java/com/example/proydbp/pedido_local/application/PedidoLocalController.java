package com.example.proydbp.pedido_local.application;

import com.example.proydbp.pedido_local.domain.PedidoLocalService;
import com.example.proydbp.pedido_local.dto.PatchPedidoLocalDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalRequestDto;
import com.example.proydbp.pedido_local.dto.PedidoLocalResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidoLocal")
public class PedidoLocalController {

    @Autowired
    private PedidoLocalService pedidoLocalService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public PedidoLocalResponseDto getPedidoLocalById(@PathVariable Long id) {
        return pedidoLocalService.findPedidoLocalById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<PedidoLocalResponseDto> getAllPedidoLocals() {
        return pedidoLocalService.findAllPedidoLocals();
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping
    public PedidoLocalResponseDto createPedidoLocal(@Valid @RequestBody PedidoLocalRequestDto dto) {
        return pedidoLocalService.createPedidoLocal(dto);
    }

    @PreAuthorize("hasRole('ROLE_MESERO') ")
    @DeleteMapping("/{id}")
    public void deletePedidoLocal(@PathVariable Long id) {
        pedidoLocalService.deletePedidoLocal(id);
    }

    @PreAuthorize("hasRole('ROLE_MESERO')")
    @PatchMapping("/{id}/entregado")
    public PedidoLocalResponseDto entregadoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.entregadoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('ROLE_MESERO')")
    @PatchMapping("/{id}/cancelado")
    public PedidoLocalResponseDto canceladoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.canceladoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PatchMapping("/{id}")
    public PedidoLocalResponseDto updatePedidoLocal(@PathVariable Long id, @RequestBody PatchPedidoLocalDto dto) {
        return pedidoLocalService.updatePedidoLocal(id, dto);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PatchMapping("/{id}/cocinando")
    public PedidoLocalResponseDto cocinandoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.cocinandoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PatchMapping("/{id}/listo")
    public PedidoLocalResponseDto listoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.listoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @GetMapping("/current")
    public List<PedidoLocalResponseDto> getPedidosLocalesActuales() {
        return pedidoLocalService.findPedidosLocalesActuales();
    }
}

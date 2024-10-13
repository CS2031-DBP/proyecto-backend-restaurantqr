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

    @Autowired
    private PedidoLocalService pedidoLocalService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public PedidoLocalResponseDto getPedidoLocalById(@PathVariable Long id) {
        return pedidoLocalService.findPedidoLocalById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<PedidoLocalResponseDto> getAllPedidoLocals() {
        return pedidoLocalService.findAllPedidoLocals();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PedidoLocalResponseDto createPedidoLocal(@RequestBody PedidoLocalRequestDto dto) {
        return pedidoLocalService.createPedidoLocal(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePedidoLocal(@PathVariable Long id) {
        pedidoLocalService.deletePedidoLocal(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public PedidoLocalResponseDto updatePedidoLocal(@PathVariable Long id, @RequestBody PatchPedidoLocalDto dto) {
        return pedidoLocalService.updatePedidoLocal(id, dto);
    }

    @PreAuthorize("hasRole('CHEF')")
    @PatchMapping("/cocinando/{id}")
    public PedidoLocalResponseDto cocinandoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.cocinandoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('CHEF')")
    @PatchMapping("/listo/{id}")
    public PedidoLocalResponseDto listoPedidoLocal(@PathVariable Long id) {
        return pedidoLocalService.listoPedidoLocal(id);
    }

    @PreAuthorize("hasRole('CHEF')")
    @GetMapping("/pedidosLocalesRecibidos")
    public List<PedidoLocalResponseDto> getPedidosLocalesRecibidos() {
        return pedidoLocalService.findPedidosLocalesRecibidos();
    }
}

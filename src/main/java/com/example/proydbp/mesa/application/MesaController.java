package com.example.proydbp.mesa.application;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mesa")
public class MesaController {

    private final MesaService mesaService;

    @Autowired
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<MesaResponseDto>> getAllTables() {
        List<MesaResponseDto> tables = mesaService.findAllMesas();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MesaResponseDto> getTableById(@PathVariable Long id) {
        MesaResponseDto table = mesaService.getMesaById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Mesa> createTable(@RequestBody @Valid MesaRequestDto tableDto) {
        Mesa newTable = mesaService.createMesa(tableDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTable.getId())
                .toUri();
        return ResponseEntity.created(location).body(newTable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Mesa> updateTable(@PathVariable Long id, @RequestBody @Valid MesaRequestDto tableDto) {
        Mesa updatedTable = mesaService.updateMesa(id, tableDto);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        mesaService.deleteMesaById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/available")
    public ResponseEntity<List<MesaResponseDto>> getAvailableTables() {
        List<MesaResponseDto> availableTables = mesaService.getAvailableMesas();
        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<MesaResponseDto>> getTablesByCapacity(@PathVariable int capacity) {
        List<MesaResponseDto> tablesByCapacity = mesaService.getMesasByCapacity(capacity);
        return new ResponseEntity<>(tablesByCapacity, HttpStatus.OK);
    }

    // adicional
    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/reservacionesMesa/{idMesa}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsDeMesa(@PathVariable Long idMesa) {
        List<ReservationResponseDto> reservations = mesaService.getReservationsDeMesa(idMesa);
        return ResponseEntity.ok(reservations);
    }


}

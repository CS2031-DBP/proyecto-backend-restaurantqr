package com.example.proydbp.mesa.application;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.reservation.domain.ReservationService;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
public class MesaController {

    private final MesaService mesaService;

    @Autowired
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MesaResponseDto>> getAllTables() {
        List<MesaResponseDto> tables = mesaService.findAllMesas();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<MesaResponseDto> getTableById(@PathVariable Long id) {
        MesaResponseDto table = mesaService.getMesaById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Mesa> createTable(@RequestBody MesaRequestDto tableDto) {
        Mesa newTable = mesaService.createMesa(tableDto);

        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> updateTable(@PathVariable Long id, @RequestBody MesaRequestDto tableDto) {
        Mesa updatedTable = mesaService.updateMesa(id, tableDto);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        mesaService.deleteMesaById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('MESERO')")
    @GetMapping("/available")
    public ResponseEntity<List<MesaResponseDto>> getAvailableTables() {
        List<MesaResponseDto> availableTables = mesaService.getAvailableMesas();
        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MESERO')")
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<MesaResponseDto>> getTablesByCapacity(@PathVariable int capacity) {
        List<MesaResponseDto> tablesByCapacity = mesaService.getMesasByCapacity(capacity);
        return new ResponseEntity<>(tablesByCapacity, HttpStatus.OK);
    }

    // adicional
    @PreAuthorize("hasRole('MESERO')")
    @GetMapping("/mesa/{idMesa}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsDeMesa(@PathVariable Long idMesa) {
        List<ReservationResponseDto> reservations = mesaService.getReservationsDeMesa(idMesa);
        return ResponseEntity.ok(reservations);
    }
}

package com.example.proydbp.mesa.application;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.mesa.domain.MesaService;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController
@RequestMapping("/mesa")
public class MesaController {

    private final MesaService mesaService;

    @Autowired
    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
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




    //Paginación:
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<MesaResponseDto>> getAllTables(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(mesaService.findAllMesas(page, size));
    }


    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/available")
    public ResponseEntity<Page<MesaResponseDto>> getAvailableTables(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(mesaService.getAvailableMesas(page, size));
    }

    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<Page<MesaResponseDto>> getTablesByCapacity(@PathVariable int capacity, @RequestParam int page, @RequestParam int size) {

        return ResponseEntity.ok(mesaService.getMesasByCapacity(capacity, page, size));
    }

    // adicional
    @PreAuthorize("hasRole('ROLE_MESERO') or hasRole('ROLE_ADMIN')")
    @GetMapping("/reservacionesMesa/{idMesa}")
    public ResponseEntity<Page<ReservationResponseDto>> getReservationsDeMesa(@PathVariable Long idMesa, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(mesaService.getReservationsDeMesa(idMesa, page, size));
    }



}

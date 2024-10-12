package com.example.proydbp.mesa.application;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.mesa.domain.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<MesaResponseDto>> getAllTables() {
        List<MesaResponseDto> tables = mesaService.findAllMesas();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MesaResponseDto> getTableById(@PathVariable Long id) {
        MesaResponseDto table = mesaService.getMesaById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mesa> createTable(@RequestBody MesaRequestDto tableDto) {
        Mesa newTable = mesaService.createMesa(tableDto);

        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> updateTable(@PathVariable Long id, @RequestBody MesaRequestDto tableDto) {
        Mesa updatedTable = mesaService.updateMesa(id, tableDto);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        mesaService.deleteMesaById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MesaResponseDto>> getAvailableTables() {
        List<MesaResponseDto> availableTables = mesaService.getAvailableMesas();
        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<MesaResponseDto>> getTablesByCapacity(@PathVariable int capacity) {
        List<MesaResponseDto> tablesByCapacity = mesaService.getMesasByCapacity(capacity);
        return new ResponseEntity<>(tablesByCapacity, HttpStatus.OK);
    }
}

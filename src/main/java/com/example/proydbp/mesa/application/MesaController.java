package com.example.proydbp.mesa.application;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
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
    public ResponseEntity<List<Table>> getAllTables() {
        List<Table> tables = mesaService.getAllTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table> getTableById(@PathVariable Long id) {
        Table table = mesaService.getTableById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Table> createTable(@RequestBody TableDto tableDto) {
        Table newTable = mesaService.createTable(tableDto);
        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Table> updateTable(@PathVariable Long id, @RequestBody TableDto tableDto) {
        Table updatedTable = mesaService.updateTable(id, tableDto);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Table> updateTableAvailability(@PathVariable Long id, @RequestBody Boolean isAvailable) {
        Table updatedTable = mesaService.updateTableAvailability(id, isAvailable);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        mesaService.deleteTable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Table>> getAvailableTables() {
        List<Table> availableTables = mesaService.getAvailableTables();
        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Table>> getTablesByLocation(@PathVariable String location) {
        List<Table> tablesByLocation = mesaService.getTablesByLocation(location);
        return new ResponseEntity<>(tablesByLocation, HttpStatus.OK);
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<Table>> getTablesByCapacity(@PathVariable int capacity) {
        List<Table> tablesByCapacity = mesaService.getTablesByCapacity(capacity);
        return new ResponseEntity<>(tablesByCapacity, HttpStatus.OK);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsForTable(@PathVariable Long id) {
        List<Reservation> reservations = mesaService.getReservationsForTable(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersForTable(@PathVariable Long id) {
        List<Order> orders = mesaService.getOrdersForTable(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}

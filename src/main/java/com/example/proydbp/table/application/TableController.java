package com.example.proydbp.table.application;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.table.domain.Table;
import com.example.proydbp.table.domain.TableService;
import com.example.proydbp.table.dto.TableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<Table>> getAllTables() {
        List<Table> tables = tableService.getAllTables();
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table> getTableById(@PathVariable Long id) {
        Table table = tableService.getTableById(id);
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Table> createTable(@RequestBody TableDto tableDto) {
        Table newTable = tableService.createTable(tableDto);
        return new ResponseEntity<>(newTable, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Table> updateTable(@PathVariable Long id, @RequestBody TableDto tableDto) {
        Table updatedTable = tableService.updateTable(id, tableDto);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Table> updateTableAvailability(@PathVariable Long id, @RequestBody Boolean isAvailable) {
        Table updatedTable = tableService.updateTableAvailability(id, isAvailable);
        return new ResponseEntity<>(updatedTable, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Table>> getAvailableTables() {
        List<Table> availableTables = tableService.getAvailableTables();
        return new ResponseEntity<>(availableTables, HttpStatus.OK);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Table>> getTablesByLocation(@PathVariable String location) {
        List<Table> tablesByLocation = tableService.getTablesByLocation(location);
        return new ResponseEntity<>(tablesByLocation, HttpStatus.OK);
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<Table>> getTablesByCapacity(@PathVariable int capacity) {
        List<Table> tablesByCapacity = tableService.getTablesByCapacity(capacity);
        return new ResponseEntity<>(tablesByCapacity, HttpStatus.OK);
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsForTable(@PathVariable Long id) {
        List<Reservation> reservations = tableService.getReservationsForTable(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrdersForTable(@PathVariable Long id) {
        List<Order> orders = tableService.getOrdersForTable(id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}

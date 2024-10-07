package com.example.proydbp.table.domain;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.table.dto.TableDto;
import com.example.proydbp.table.infrastructure.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {

    private final TableRepository tableRepository;

    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }

    public Table getTableById(Long id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id " + id));
    }

    public Table createTable(TableDto tableDto) {
        Table newTable = new Table();
        newTable.setQrCode(tableDto.getQrCode());
        newTable.setLocation(tableDto.getLocation());
        newTable.setCapacity(tableDto.getCapacity());
        newTable.setAvailable(tableDto.isAvailable());
        return tableRepository.save(newTable);
    }


    public Table updateTable(Long id, TableDto tableDto) {
        Table existingTable = getTableById(id);
        existingTable.setQrCode(tableDto.getQrCode());
        existingTable.setLocation(tableDto.getLocation());
        existingTable.setCapacity(tableDto.getCapacity());
        existingTable.setAvailable(tableDto.isAvailable());
        return tableRepository.save(existingTable);
    }

    public Table updateTableAvailability(Long id, Boolean isAvailable) {
        Table existingTable = getTableById(id);
        existingTable.setAvailable(isAvailable);
        return tableRepository.save(existingTable);
    }

    public void deleteTable(Long id) {
        Table existingTable = getTableById(id);
        tableRepository.delete(existingTable);
    }

    public List<Table> getAvailableTables() {
        return tableRepository.findByAvailableTrue();
    }

    public List<Table> getTablesByLocation(String location) {
        return tableRepository.findByLocation(location);
    }

    public List<Table> getTablesByCapacity(int capacity) {
        return tableRepository.findByCapacityGreaterThanEqual(capacity);
    }

    public List<Reservation> getReservationsForTable(Long id) {
        Table table = getTableById(id);
        return table.getReservations();
    }

    public List<Order> getOrdersForTable(Long id) {
        Table table = getTableById(id);
        return table.getOrders();
    }
}

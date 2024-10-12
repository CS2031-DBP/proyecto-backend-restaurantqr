package com.example.proydbp.mesa.domain;

import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;

    @Autowired
    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<Table> getAllTables() {
        return mesaRepository.findAll();
    }

    public Table getTableById(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id " + id));
    }

    public Table createTable(TableDto tableDto) {
        Table newTable = new Table();
        newTable.setQrCode(tableDto.getQrCode());
        newTable.setLocation(tableDto.getLocation());
        newTable.setCapacity(tableDto.getCapacity());
        newTable.setAvailable(tableDto.isAvailable());
        return mesaRepository.save(newTable);
    }


    public Table updateTable(Long id, TableDto tableDto) {
        Table existingTable = getTableById(id);
        existingTable.setQrCode(tableDto.getQrCode());
        existingTable.setLocation(tableDto.getLocation());
        existingTable.setCapacity(tableDto.getCapacity());
        existingTable.setAvailable(tableDto.isAvailable());
        return mesaRepository.save(existingTable);
    }

    public Table updateTableAvailability(Long id, Boolean isAvailable) {
        Table existingTable = getTableById(id);
        existingTable.setAvailable(isAvailable);
        return mesaRepository.save(existingTable);
    }

    public void deleteTable(Long id) {
        Table existingTable = getTableById(id);
        mesaRepository.delete(existingTable);
    }

    public List<Table> getAvailableTables() {
        return mesaRepository.findByAvailableTrue();
    }

    public List<Table> getTablesByLocation(String location) {
        return mesaRepository.findByLocation(location);
    }

    public List<Table> getTablesByCapacity(int capacity) {
        return mesaRepository.findByCapacityGreaterThanEqual(capacity);
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

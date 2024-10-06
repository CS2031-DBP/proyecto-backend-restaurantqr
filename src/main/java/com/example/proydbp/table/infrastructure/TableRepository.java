package com.example.proydbp.table.infrastructure;

import com.example.proydbp.table.domain.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {

    List<Table> findByAvailableTrue();     // Método para encontrar mesas disponibles

    List<Table> findByLocation(String location);     // Método para encontrar mesas por ubicación

    List<Table> findByCapacityGreaterThanEqual(Integer capacity);  // Método para encontrar mesas por capacidad mínima
}

package com.example.proydbp.mesa.infrastructure;

import com.example.proydbp.mesa.domain.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByAvailable(Boolean isAvailable);
    List<Mesa> findByCapacity(int capacity);
    Optional<Mesa> findById(Long id);
}

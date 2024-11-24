package com.example.proydbp.mesa.infrastructure;

import com.example.proydbp.mesa.domain.Mesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Page<Mesa> findByAvailable(boolean available, Pageable pageable);
    Page<Mesa> findByCapacity(int capacity, Pageable pageable);
    Optional<Mesa> findById(Long id);
}

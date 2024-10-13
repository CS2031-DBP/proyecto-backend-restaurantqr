package com.example.proydbp.mesa.infrastructure;

import com.example.proydbp.mesa.domain.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    boolean existsByNumero(Integer numero);

    List<Mesa> findByAvailableTrue();
    List<Mesa> findByCapacity(int capacity);
}

package com.example.proydbp.repartidor.infrastructure;

import com.example.proydbp.repartidor.domain.Repartidor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

}

package com.example.proydbp.repartidor.infrastructure;

import com.example.proydbp.repartidor.domain.Repartidor;

import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepartidorRepository extends BaseUserRepository<Repartidor> {

}

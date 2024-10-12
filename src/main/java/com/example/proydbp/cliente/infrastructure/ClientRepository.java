package com.example.proydbp.cliente.infrastructure;

import com.example.proydbp.cliente.domain.Client;
import com.example.proydbp.user.infrastructure.BaseUserRepository;

import java.util.Optional;

public interface ClientRepository extends BaseUserRepository<Client> {
    Optional<Client> findByEmail(String email);
}

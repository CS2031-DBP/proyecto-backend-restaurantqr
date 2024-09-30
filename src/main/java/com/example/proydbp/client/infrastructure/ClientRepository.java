package com.example.proydbp.client.infrastructure;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.user.infrastructure.BaseUserRepository;

import java.util.Optional;

public interface ClientRepository extends BaseUserRepository<Client> {
    Optional<Client> findByEmail(String email);
}

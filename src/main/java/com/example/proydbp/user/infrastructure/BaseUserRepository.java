package com.example.proydbp.user.infrastructure;

import com.example.proydbp.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository <T extends User> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);
    User getUserByEmail(String email);
}

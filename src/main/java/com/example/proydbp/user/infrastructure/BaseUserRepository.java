package com.example.proydbp.user.infrastructure;

import com.example.proydbp.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository <T extends User> extends JpaRepository<T, Long> {
}

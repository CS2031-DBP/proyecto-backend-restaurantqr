package com.example.proydbp.reviewMesero.infrastructure;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;

public interface ReviewMeseroRepository extends JpaRepository<ReviewMesero, Long> {
    boolean existsByMeseroAndClientAndFecha(Mesero mesero, Client client, ZonedDateTime fecha);
}

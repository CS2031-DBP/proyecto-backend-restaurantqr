package com.example.proydbp.reservation.infrastructure;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    Page<Reservation> findByMesa(Mesa mesa, Pageable pageable);

    List<Reservation> findByMesaAndFechaBetween(Mesa mesa, ZonedDateTime start, ZonedDateTime end);
}
package com.example.proydbp.reservation.infrastructure;

import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMesa(Mesa mesa);
}

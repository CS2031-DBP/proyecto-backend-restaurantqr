package com.example.proydbp.reservation.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.reservation.dto.ReservationDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MesaRepository mesaRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              MesaRepository mesaRepository) {
        this.reservationRepository = reservationRepository;
        this.mesaRepository = mesaRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id " + id));
    }

    public List<Reservation> getReservationsByClientId(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public Reservation createReservation(ReservationDto reservationDto) {
        Reservation newReservation = new Reservation();
        return getReservation(reservationDto, newReservation);
    }

    @NotNull
    private Reservation getReservation(ReservationDto reservationDto, Reservation newReservation) {
        newReservation.setReservationDate(reservationDto.getReservationDate());
        newReservation.setReservationTime(reservationDto.getReservationTime());
        newReservation.setNumOfPeople(reservationDto.getNumOfPeople());

        Mesa table = mesaRepository.findById(reservationDto.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationDto.getTableId()));

        newReservation.setTable(table);
        newReservation.setStatus(reservationDto.getStatus());
        newReservation.setSpecialRequests(reservationDto.getSpecialRequests());

        return reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long id, ReservationDto reservationDto) {
        Reservation existingReservation = getReservationById(id);

        return getReservation(reservationDto, existingReservation);
    }


    public Reservation patchReservation(Long id, Reservation updatedFields) {
        Reservation existingReservation = getReservationById(id);

        if (updatedFields.getReservationDate() != null) {
            existingReservation.setReservationDate(updatedFields.getReservationDate());
        }
        if (updatedFields.getReservationTime() != null) {
            existingReservation.setReservationTime(updatedFields.getReservationTime());
        }
        if (updatedFields.getNumOfPeople() != null) {
            existingReservation.setNumOfPeople(updatedFields.getNumOfPeople());
        }
        if (updatedFields.getTable() != null) {
            existingReservation.setTable(updatedFields.getTable());
        }
        if (updatedFields.getStatus() != null) {
            existingReservation.setStatus(updatedFields.getStatus());
        }
        if (updatedFields.getSpecialRequests() != null) {
            existingReservation.setSpecialRequests(updatedFields.getSpecialRequests());
        }

        return reservationRepository.save(existingReservation);
    }

    public void deleteReservation(Long id) {
        Reservation existingReservation = getReservationById(id);
        reservationRepository.delete(existingReservation);
    }
}

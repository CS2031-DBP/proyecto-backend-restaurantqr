package com.example.proydbp.reservation.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.domain.Mesa;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
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

    public Reservation createReservation(ReservationResponseDto reservationResponseDto) {
        Reservation newReservation = new Reservation();
        return getReservation(reservationResponseDto, newReservation);
    }

    @NotNull
    private Reservation getReservation(ReservationResponseDto reservationResponseDto, Reservation newReservation) {
        newReservation.setReservationDate(reservationResponseDto.getReservationDate());
        newReservation.setReservationTime(reservationResponseDto.getReservationTime());
        newReservation.setNumOfPeople(reservationResponseDto.getNumOfPeople());

        Mesa table = mesaRepository.findById(reservationResponseDto.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + reservationResponseDto.getTableId()));

        newReservation.setTable(table);
        newReservation.setStatusReservation(reservationResponseDto.getStatusReservation());
        newReservation.setSpecialRequests(reservationResponseDto.getSpecialRequests());

        return reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long id, ReservationResponseDto reservationResponseDto) {
        Reservation existingReservation = getReservationById(id);

        return getReservation(reservationResponseDto, existingReservation);
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
        if (updatedFields.getStatusReservation() != null) {
            existingReservation.setStatusReservation(updatedFields.getStatusReservation());
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

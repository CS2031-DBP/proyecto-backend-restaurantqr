package com.example.proydbp.reservation.domain;

import com.example.proydbp.reservation.dto.ReservationDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import com.example.proydbp.table.domain.Table;
import com.example.proydbp.table.infrastructure.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              TableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id " + id));
    }

    public List<Reservation> getReservationsByClientId(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public Reservation createReservation(ReservationDto reservationDto) {
        Reservation newReservation = new Reservation();
        newReservation.setReservationDate(reservationDto.getReservationDate());
        newReservation.setReservationTime(reservationDto.getReservationTime());
        newReservation.setNumOfPeople(reservationDto.getNumOfPeople());

        Table table = tableRepository.findById(reservationDto.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found with id " + reservationDto.getTableId()));

        newReservation.setTable(table);
        newReservation.setStatus(reservationDto.getStatus());
        newReservation.setSpecialRequests(reservationDto.getSpecialRequests());

        return reservationRepository.save(newReservation);
    }

    public Reservation updateReservation(Long id, ReservationDto reservationDto) {
        Reservation existingReservation = getReservationById(id);

        existingReservation.setReservationDate(reservationDto.getReservationDate());
        existingReservation.setReservationTime(reservationDto.getReservationTime());
        existingReservation.setNumOfPeople(reservationDto.getNumOfPeople());

        Table table = tableRepository.findById(reservationDto.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found with id " + reservationDto.getTableId()));

        existingReservation.setTable(table);
        existingReservation.setStatus(reservationDto.getStatus());
        existingReservation.setSpecialRequests(reservationDto.getSpecialRequests());

        return reservationRepository.save(existingReservation);
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

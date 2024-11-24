package com.example.proydbp.mesa.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;

    public MesaService(MesaRepository mesaRepository, ModelMapper modelMapper,
                       ReservationRepository reservationRepository) {
        this.mesaRepository = mesaRepository;
        this.modelMapper = modelMapper;
        this.reservationRepository = reservationRepository;
    }

    public Page<MesaResponseDto> findAllMesas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Crea un objeto Pageable con los parámetros de paginación

        // Recupera las mesas paginadas desde el repositorio
        Page<Mesa> mesasPage = mesaRepository.findAll(pageable);

        // Convierte las entidades Mesa a MesaResponseDto y devuelve el Page con los DTOs
        return mesasPage.map(mesa -> modelMapper.map(mesa, MesaResponseDto.class));
    }

    public MesaResponseDto getMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con id " + id + " no encontrada"));
        return modelMapper.map(mesa, MesaResponseDto.class);
    }

    public Mesa createMesa(MesaRequestDto request) {

        if (request.getCapacity() <= 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a 1.");
        }

        Mesa newMesa = new Mesa();
        newMesa.setAvailable(true);
        newMesa.setCapacity(request.getCapacity());
        String ip = "192.168.1.100";
        newMesa.setQr("https://" + ip + "auth/login/");
        newMesa.setReservations(new ArrayList<>());
        return mesaRepository.save(newMesa);
    }

    public Mesa updateMesa(Long id, MesaRequestDto mesaDto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con id " + id + " no encontrada"));

        if (mesaDto.getCapacity() <= 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a 1.");
        }
        mesa.setCapacity(mesaDto.getCapacity());
        mesa.setAvailable(mesaDto.isAvailable());

        return mesaRepository.save(mesa);
    }

    public Mesa deleteMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con id " + id + " no encontrada"));
        mesaRepository.delete(mesa);
        return mesa;
    }

    public Page<MesaResponseDto> getAvailableMesas(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Crea un objeto Pageable con los parámetros de paginación

        // Recupera las mesas disponibles paginadas desde el repositorio
        Page<Mesa> mesasPage = mesaRepository.findByAvailable(true, pageable);

        // Convierte las entidades Mesa a MesaResponseDto y devuelve el Page con los DTOs
        return mesasPage.map(mesa -> modelMapper.map(mesa, MesaResponseDto.class));
    }

    public Page<MesaResponseDto> getMesasByCapacity(int capacity, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Crea el objeto Pageable

        // Consulta paginada por capacidad
        Page<Mesa> mesasPage = mesaRepository.findByCapacity(capacity, pageable);

        // Mapea las entidades Mesa a MesaResponseDto y devuelve la página
        return mesasPage.map(mesa -> modelMapper.map(mesa, MesaResponseDto.class));
    }


    // adicional

    public Page<ReservationResponseDto> getReservationsDeMesa(Long idMesa, int page, int size) {
        // Buscar la mesa por su ID
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + idMesa + " no encontrada"));

        // Crear el objeto Pageable para la paginación
        Pageable pageable = PageRequest.of(page, size);

        // Buscar las reservas asociadas a esa mesa, con paginación
        Page<Reservation> reservationsPage = reservationRepository.findByMesa(mesa, pageable);

        // Mapear las entidades Reservation a ReservationResponseDto y devolver la página
        return reservationsPage.map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class));
    }


    public void changeAvailability(Long id){
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con id " + id + " no encontrada"));
        mesa.setAvailable(!mesa.isAvailable());
        mesaRepository.save(mesa);

    }
}

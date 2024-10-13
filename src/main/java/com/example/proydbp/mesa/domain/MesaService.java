package com.example.proydbp.mesa.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import org.hibernate.type.TrueFalseConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;

    // Inyección de dependencias
    public MesaService(MesaRepository mesaRepository, ModelMapper modelMapper,
                       ReservationRepository reservationRepository) {
        this.mesaRepository = mesaRepository;
        this.modelMapper = modelMapper;
        this.reservationRepository = reservationRepository;
    }

    public List<MesaResponseDto> findAllMesas() {

        List<Mesa> mesas = mesaRepository.findAll();

        return mesas.stream()
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }

    public MesaResponseDto getMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa not found with id " + id));
        return modelMapper.map(mesa, MesaResponseDto.class);
    }

    public Mesa createMesa(MesaRequestDto request) {
        if (mesaRepository.existsByNumero(request.getNumero())) {
            throw new IllegalArgumentException("La mesa con el número " + request.getNumero() + " ya existe.");
        }
        if (request.getCapacity() <= 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a 1.");
        }
        Mesa newMesa = new Mesa();
        newMesa.setAvailable(true);
        newMesa.setNumero(request.getNumero());
        newMesa.setCapacity(request.getCapacity());
        String ip = "192.168.1.100";
        newMesa.setQr("https://" + ip + "auth/login/");
        return mesaRepository.save(newMesa);
    }


    public Mesa updateMesa(Long id, MesaRequestDto mesaDto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa not found with id " + id));
        if (mesaRepository.existsByNumero(mesaDto.getNumero())) {
            throw new IllegalArgumentException("La mesa con el número " + mesaDto.getNumero() + " ya existe.");
        }
        if (mesaDto.getCapacity() <= 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a 1.");
        }
        mesa.setNumero(mesaDto.getNumero());
        mesa.setCapacity(mesaDto.getCapacity());

        return mesaRepository.save(mesa);
    }

    public Mesa deleteMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa not found with id " + id));
        mesaRepository.delete(mesa);
        return mesa;
    }


    public List<MesaResponseDto> getAvailableMesas() {
        List<Mesa> mesas = mesaRepository.findByAvailableTrue();

        return mesas.stream()
                .filter(Mesa::isAvailable)  // Filtrar solo las mesas disponibles
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }


    public List<MesaResponseDto> getMesasByCapacity(int capacity) {
        List<Mesa> mesas = mesaRepository.findByCapacity(capacity);

        return mesas.stream()
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }

    // adicional

    public List<ReservationResponseDto> getReservationsDeMesa(Long idMesa) {
        // Buscar la mesa por su ID
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa not found with id " + idMesa));

        // Buscar las reservas asociadas a esa mesa
        List<Reservation> reservations = reservationRepository.findByMesa(mesa);

        // Mapear las entidades de Reservation a ReservationResponseDto
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }


}

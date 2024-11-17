package com.example.proydbp.mesa.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.reservation.dto.ReservationResponseDto;
import com.example.proydbp.reservation.infrastructure.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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



    public List<MesaResponseDto> findAllMesas() {

        List<Mesa> mesas = mesaRepository.findAll();

        return mesas.stream()
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }

    public MesaResponseDto getMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + id + " no encontrada"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + id + " no encontrada"));

        if (mesaDto.getCapacity() <= 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor o igual a 1.");
        }
        mesa.setCapacity(mesaDto.getCapacity());
        mesa.setAvailable(mesaDto.isAvailable());

        return mesaRepository.save(mesa);
    }

    public Mesa deleteMesaById(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + id + " no encontrada"));
        mesaRepository.delete(mesa);
        return mesa;
    }


    public List<MesaResponseDto> getAvailableMesas() {
        List<Mesa> mesas = mesaRepository.findByAvailable(true);

        return mesas.stream()
                .filter(Mesa::isAvailable)
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
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + idMesa + " no encontrada"));

        // Buscar las reservas asociadas a esa mesa
        List<Reservation> reservations = reservationRepository.findByMesa(mesa);

        // Mapear las entidades de Reservation a ReservationResponseDto
        return reservations.stream()
                .map(reservation -> modelMapper.map(reservation, ReservationResponseDto.class))
                .collect(Collectors.toList());
    }

    public void changeAvailability(Long id){
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa con " + id + " no encontrada"));
        mesa.setAvailable(!mesa.isAvailable());
        mesaRepository.save(mesa);

    }


}

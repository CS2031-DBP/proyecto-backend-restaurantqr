package com.example.proydbp.mesa.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.dto.MesaRequestDto;
import com.example.proydbp.mesa.dto.MesaResponseDto;
import com.example.proydbp.order.domain.Order;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
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

    // Inyección de dependencias
    public MesaService(MesaRepository mesaRepository, ModelMapper modelMapper) {
        this.mesaRepository = mesaRepository;
        this.modelMapper = modelMapper;
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
        newMesa.setQr("http://" + ip + "/pedidoLocal/" + request.getNumero());
        return mesaRepository.save(newMesa);
    }


    public Mesa updateMesa(Long id, MesaRequestDto mesaDto) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa not found with id " + id));

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
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }


    public List<MesaResponseDto> getMesasByCapacity(int capacity) {
        List<Mesa> mesas = mesaRepository.findByCapacity(capacity);

        return mesas.stream()
                .map(mesa -> modelMapper.map(mesa, MesaResponseDto.class))
                .collect(Collectors.toList());
    }

}

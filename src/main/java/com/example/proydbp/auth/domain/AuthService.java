package com.example.proydbp.auth.domain;

import com.example.proydbp.auth.dto.AuthResponseDto;
import com.example.proydbp.auth.dto.LoginRequestDto;
import com.example.proydbp.auth.dto.RegisterRequestDto;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.config.JwtService;
import com.example.proydbp.events.email_event.BienvenidaChefEvent;
import com.example.proydbp.events.email_event.BienvenidaClienteEvent;
import com.example.proydbp.events.email_event.BienvenidaMeseroEvent;
import com.example.proydbp.events.email_event.BienvenidaRepartidorEvent;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final BaseUserRepository<User> userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AuthService(BaseUserRepository<User> userRepository, JwtService jwtService,
                       PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public AuthResponseDto login(LoginRequestDto req) {
        Optional<User> user;
        user = userRepository.findByEmail(req.getEmail());

        if (user.isEmpty()) throw new UsernameNotFoundException("El correo electrónico no está registrado");

        if (!passwordEncoder.matches(req.getPassword(), user.get().getPassword()))
            throw new IllegalArgumentException("La contraseña es incorrecta");

        AuthResponseDto response = new AuthResponseDto();

        response.setToken(jwtService.generateToken(user.get()));
        return response;
    }

    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        Optional<User> user = userRepository.findByEmail(registerRequestDto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        Client cliente = new Client();
        cliente.setCreatedAt(ZonedDateTime.now());
        cliente.setRole(Role.CLIENT);
        cliente.setFirstName(registerRequestDto.getFirstName());
        cliente.setLastName(registerRequestDto.getLastName());
        cliente.setEmail(registerRequestDto.getEmail());
        cliente.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        cliente.setPhoneNumber(registerRequestDto.getPhone());
        cliente.setUpdatedAt(ZonedDateTime.now());
        cliente.setRango(Rango.BRONZE);
        userRepository.save(cliente);

        // Disparar evento de bienvenida
        BienvenidaClienteEvent bienvenidaEvent = new BienvenidaClienteEvent(cliente, cliente.getEmail());
        eventPublisher.publishEvent(bienvenidaEvent);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtService.generateToken(cliente));
        return response;
    }

    public AuthResponseDto registerAdmin(RegisterRequestDto registerRequestDto) {
        Optional<User> user = userRepository.findByEmail(registerRequestDto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        User admin = new User();
        admin.setCreatedAt(ZonedDateTime.now());
        admin.setRole(Role.ADMIN);
        admin.setFirstName(registerRequestDto.getFirstName());
        admin.setLastName(registerRequestDto.getLastName());
        admin.setEmail(registerRequestDto.getEmail());
        admin.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        admin.setPhoneNumber(registerRequestDto.getPhone());
        admin.setUpdatedAt(ZonedDateTime.now());
        userRepository.save(admin);

        AuthResponseDto response = new AuthResponseDto();

        response.setToken(jwtService.generateToken(admin));
        return response;
    }

    public AuthResponseDto registerChef(RegisterRequestDto registerRequestDto) {
        Optional<User> user = userRepository.findByEmail(registerRequestDto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        User chef = new User();
        chef.setCreatedAt(ZonedDateTime.now());
        chef.setRole(Role.CHEF);
        chef.setFirstName(registerRequestDto.getFirstName());
        chef.setLastName(registerRequestDto.getLastName());
        chef.setEmail(registerRequestDto.getEmail());
        chef.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        chef.setPhoneNumber(registerRequestDto.getPhone());
        chef.setUpdatedAt(ZonedDateTime.now());
        userRepository.save(chef);

        // Disparar evento
        eventPublisher.publishEvent(new BienvenidaChefEvent(chef, chef.getEmail()));

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtService.generateToken(chef));
        return response;
    }


    public AuthResponseDto registerMesero(RegisterRequestDto registerRequestDto) {
        Optional<User> user = userRepository.findByEmail(registerRequestDto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        Mesero mesero = new Mesero();
        mesero.setCreatedAt(ZonedDateTime.now());
        mesero.setRole(Role.MESERO);
        mesero.setFirstName(registerRequestDto.getFirstName());
        mesero.setLastName(registerRequestDto.getLastName());
        mesero.setEmail(registerRequestDto.getEmail());
        mesero.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        mesero.setPhoneNumber(registerRequestDto.getPhone());
        mesero.setUpdatedAt(ZonedDateTime.now());
        userRepository.save(mesero);

        // Disparar evento
        eventPublisher.publishEvent(new BienvenidaMeseroEvent(mesero, mesero.getEmail()));

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtService.generateToken(mesero));
        return response;
    }

    public AuthResponseDto registerRepartidor(RegisterRequestDto registerRequestDto) {
        Optional<User> user = userRepository.findByEmail(registerRequestDto.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistException("El correo electrónico ya ha sido registrado");

        Repartidor repartidor = new Repartidor();
        repartidor.setCreatedAt(ZonedDateTime.now());
        repartidor.setRole(Role.REPARTIDOR);
        repartidor.setFirstName(registerRequestDto.getFirstName());
        repartidor.setLastName(registerRequestDto.getLastName());
        repartidor.setEmail(registerRequestDto.getEmail());
        repartidor.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        repartidor.setPhoneNumber(registerRequestDto.getPhone());
        repartidor.setUpdatedAt(ZonedDateTime.now());
        userRepository.save(repartidor);

        // Disparar evento
        eventPublisher.publishEvent(new BienvenidaRepartidorEvent(repartidor, repartidor.getEmail()));

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtService.generateToken(repartidor));
        return response;
    }


}

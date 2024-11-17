package com.example.proydbp.auth.domain;

import com.example.proydbp.auth.dto.AuthResponseDto;
import com.example.proydbp.auth.dto.LoginRequestDto;
import com.example.proydbp.auth.dto.RegisterRequestDto;
import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.config.JwtService;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthService(BaseUserRepository<User> userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(jwtService.generateToken(chef));
        return response;
    }
}

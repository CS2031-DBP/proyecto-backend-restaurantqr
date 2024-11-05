package com.example.proydbp.configuration.domain;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.client.domain.Rango;
import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.configuration.dto.JwtAuthenticationResponse;
import com.example.proydbp.configuration.dto.SigninRequest;
import com.example.proydbp.exception.UserAlreadyExistException;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final BaseUserRepository<User> userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public AuthenticationService(BaseUserRepository<User> userRepository, ClientRepository clientRepository,JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = new ModelMapper();
        this.clientRepository = clientRepository;
    }

    public JwtAuthenticationResponse register(User user) {
        //Falta lógica para ver si ya existe

        Client cliente = new Client();
        cliente.setPassword(passwordEncoder.encode(user.getPassword()));
        cliente.setRole(Role.CLIENT);
        cliente.setEmail(user.getEmail());
        cliente.setFirstName(user.getFirstName());
        cliente.setLastName(user.getLastName());
        cliente.setPhoneNumber(String.valueOf(user.getPhoneNumber()));
        cliente.setRango(Rango.BRONZE);
        cliente.setLoyaltyPoints(0);
        clientRepository.save(cliente);
        user.setRole(Role.CLIENT);
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(jwt);
        return response;
    }

    public JwtAuthenticationResponse login(SigninRequest request) throws IllegalArgumentException {

        Optional<User> user;
        user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not registered");

        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
            throw new IllegalArgumentException("Password is incorrect");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse();

        response.setToken(jwtService.generateToken(user.get()));
        return response;
    }
}

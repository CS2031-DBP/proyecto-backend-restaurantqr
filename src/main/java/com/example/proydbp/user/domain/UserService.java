package com.example.proydbp.user.domain;

import com.example.proydbp.client.infrastructure.ClientRepository;
import com.example.proydbp.delivery.infrastructure.DeliveryRepository;
import com.example.proydbp.mesero.domain.Mesero;
import com.example.proydbp.mesero.infrastructure.MeseroRepository;
import com.example.proydbp.repartidor.domain.Repartidor;
import com.example.proydbp.repartidor.infrastructure.RepartidorRepository;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private BaseUserRepository<User> baseUserRepository;

    @Autowired
    private MeseroRepository meseroRepository;

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Autowired
    private ClientRepository clientRepository;

    //Devuelve el
    public User findByEmail(String username, String role) {
        User user;
        if (role.equals("ROLE_MESERO"))
            user = meseroRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        else if (role.equals("ROLE_CLIENT")) {
            user = clientRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        }
        else if (role.equals("ROLE_REPARTIDOR")) {
            user = repartidorRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        }
        else
            user = baseUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return user;
    }

    public UserDetailsService userDetailsService() {
        return username -> {
            User user = baseUserRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            return (UserDetails) user;
        };
    }









}

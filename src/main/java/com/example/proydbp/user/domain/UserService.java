package com.example.proydbp.user.domain;

import com.example.proydbp.exception.ResourceNotFoundException;
import com.example.proydbp.mesa.infrastructure.MesaRepository;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {
    private final BaseUserRepository baseUserRepository;

    @Bean(name = "UserDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
                User user = baseUserRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return (UserDetails) user;
        };
    }

}

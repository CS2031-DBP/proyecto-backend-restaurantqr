package com.example.proydbp.user;

import com.example.proydbp.configuration.controller.AuthController;
import com.example.proydbp.configuration.domain.AuthenticationService;
import com.example.proydbp.configuration.dto.JwtAuthenticationResponse;
import com.example.proydbp.configuration.dto.SigninRequest;
import com.example.proydbp.user.domain.Role;
import com.example.proydbp.user.domain.User;
import com.example.proydbp.user.domain.UserService;
import com.example.proydbp.user.infrastructure.BaseUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class userTest {
    private User user;
    private SigninRequest signinRequest;
    private JwtAuthenticationResponse jwtResponse;

    @Mock
    private BaseUserRepository baseUserRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole(Role.CLIENT); // Asumiendo que Role es un enum
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setPhoneNumber("123456789");
        user.setAvgRating(4.5f);

        signinRequest = new SigninRequest();
        signinRequest.setEmail("john.doe@example.com");
        signinRequest.setPassword("password123");

        jwtResponse = new JwtAuthenticationResponse();
        jwtResponse.setToken("Bearer " + jwtResponse.getToken());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testSignUp() {
        // Mock del servicio de autenticación
        when(authenticationService.signup(user)).thenReturn(jwtResponse);
        ResponseEntity<JwtAuthenticationResponse> response = authController.signup(user);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtResponse, response.getBody());
    }

    @Test
    void testSigninSuccess() {
        // Mock del servicio de autenticación
        when(authenticationService.signin(signinRequest)).thenReturn(jwtResponse);
        ResponseEntity<JwtAuthenticationResponse> response = authController.signin(signinRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(jwtResponse, response.getBody());
    }


}

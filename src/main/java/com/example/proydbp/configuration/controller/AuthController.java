package com.example.proydbp.configuration.controller;

import com.example.proydbp.configuration.domain.AuthenticationService;
import com.example.proydbp.configuration.dto.JwtAuthenticationResponse;
import com.example.proydbp.configuration.dto.SigninRequest;
import com.example.proydbp.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @GetMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}

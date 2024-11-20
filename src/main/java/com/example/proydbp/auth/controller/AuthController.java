package com.example.proydbp.auth.controller;
import com.example.proydbp.auth.domain.AuthService;
import com.example.proydbp.auth.dto.AuthResponseDto;
import com.example.proydbp.auth.dto.LoginRequestDto;
import com.example.proydbp.auth.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println("Log");
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponseDto> registerAdmin(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.registerAdmin(registerRequestDto));
    }

    @PostMapping("/register/chef")
    public ResponseEntity<AuthResponseDto> registerChef(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.registerChef(registerRequestDto));
    }

    @PostMapping("/register/mesero")
    public ResponseEntity<AuthResponseDto> registerMesero(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.registerMesero(registerRequestDto));
    }

    @PostMapping("/register/repartidor")
    public ResponseEntity<AuthResponseDto> registerRepartidor(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.registerRepartidor(registerRequestDto));
    }
}

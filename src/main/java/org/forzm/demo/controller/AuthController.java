package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.*;
import org.forzm.demo.model.RefreshToken;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.VerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/login").toUriString());
        return ResponseEntity.created(location).body(authService.login(loginRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/token/refresh").toUriString());
        return ResponseEntity.created(location).body(authService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/user/{authToken}")
    public ResponseEntity<UserResponseDto> getCurrentUser(@PathVariable("authToken") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUserFromToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        authService.logout(refreshTokenRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package org.forzm.demo.controller;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.*;
import org.forzm.demo.service.AuthService;
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
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        authService.register(registerRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/login").toUriString());
        return ResponseEntity.created(location).body(authService.login(loginRequestDto));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        URI location = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/token/refresh").toUriString());
        return ResponseEntity.created(location).body(authService.refreshToken(refreshTokenRequestDto));
    }

    @GetMapping("/user/{authToken}")
    public ResponseEntity<UserResponseDto> getCurrentUser(@PathVariable("authToken") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getUserFromToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        authService.logout(refreshTokenRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

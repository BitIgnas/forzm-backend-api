package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.*;
import org.forzm.demo.exception.UserExistsException;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.RefreshTokenRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.security.JwtProvider;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.RefreshTokenService;
import org.forzm.demo.service.VerificationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final VerificationService verificationService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ModelMapper modelMapper;

    @Override
    public void register(RegisterRequestDto registerRequestDto) {
        Optional<User> userOptional = userRepository.findByUsername(registerRequestDto.getUsername());
        userOptional.ifPresent(user -> { throw new UserExistsException("User already exist");});

        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());
        user.setDateCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token = verificationService.generateVerificationToken(user);
        verificationService.sendMail(user.getEmail(), token);
    }

    @Override
    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateJwt(authenticate);
        return AuthenticationResponseDto.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(loginRequestDto.getUsername())
                .build();
    }

    @Override
    public AuthenticationResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        refreshTokenService.validateRefreshToken(refreshTokenRequestDto.getRefreshToken());
        String token = jwtProvider.generateJwtWithUsername(refreshTokenRequestDto.getUsername());

        return AuthenticationResponseDto.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequestDto.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(refreshTokenRequestDto.getUsername())
                .build();
    }

    @Override
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
    }

    @Override
    public UserResponseDto getUserFromToken(String token) {
        String username = jwtProvider.getUsernameFromJwt(token);

        return mapToResponseDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user was not found")));
    }

    @Override
    public void logout(RefreshTokenRequestDto refreshTokenRequestDto) {
        refreshTokenRepository.deleteByToken(refreshTokenRequestDto.getRefreshToken());
        SecurityContextHolder.clearContext();
    }

    @Override
    public UserResponseDto mapToResponseDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}

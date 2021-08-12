package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.dto.AuthenticationResponse;
import org.forzm.demo.dto.LoginRequest;
import org.forzm.demo.dto.RefreshTokenRequest;
import org.forzm.demo.dto.RegisterRequest;
import org.forzm.demo.exception.UserExistsException;
import org.forzm.demo.model.User;
import org.forzm.demo.repository.RefreshTokenRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.security.JwtProvider;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.RefreshTokenService;
import org.forzm.demo.service.VerificationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Override
    public void register(RegisterRequest registerRequest) {
        checkIfUserExist(registerRequest.getUsername());
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setProfileImageUrl("user-stock.png");
        user.setDateCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token = verificationService.generateVerificationToken(user);
        verificationService.sendMail(user.getEmail(), token);
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateJwt(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(loginRequest.getUsername())
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateJwtWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpiration()))
                .username(refreshTokenRequest.getUsername())
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
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenRepository.deleteByToken(refreshTokenRequest.getRefreshToken());
    }

    @Override
    public void checkIfUserExist(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {throw new UserExistsException("User already exist");});
    }
}

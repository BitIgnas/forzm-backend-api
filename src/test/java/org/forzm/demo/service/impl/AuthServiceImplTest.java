package org.forzm.demo.service.impl;

import org.checkerframework.checker.nullness.Opt;
import org.forzm.demo.dto.AuthenticationResponseDto;
import org.forzm.demo.dto.LoginRequestDto;
import org.forzm.demo.dto.RegisterRequestDto;
import org.forzm.demo.model.RefreshToken;
import org.forzm.demo.model.User;
import org.forzm.demo.model.VerificationToken;
import org.forzm.demo.repository.RefreshTokenRepository;
import org.forzm.demo.repository.UserRepository;
import org.forzm.demo.security.JwtProvider;
import org.forzm.demo.service.RefreshTokenService;
import org.forzm.demo.service.VerificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationService verificationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AuthServiceImpl authService;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;


    @Test
    void register() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("TEST", "TEST", "SECRET");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED PASSWORD");
        when(verificationService.generateVerificationToken(any(User.class))).thenReturn("TOKEN");
        doNothing().when(verificationService).sendMail(anyString(), anyString());

        authService.register(registerRequestDto);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(verificationService, times(1)).generateVerificationToken(any(User.class));
        verify(verificationService, times(1)).sendMail(anyString(), anyString());
        verifyNoMoreInteractions(verificationService);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isNotNull();
        assertThat(capturedUser).isInstanceOf(User.class);
        assertThat(capturedUser.getUsername()).isEqualTo(registerRequestDto.getUsername());
        assertThat(capturedUser.getPassword()).isEqualTo("ENCODED PASSWORD");
        assertThat(capturedUser.getEmail()).isEqualTo(registerRequestDto.getEmail());
        assertThat(capturedUser.isEnabled()).isEqualTo(false);
    }

    @Test
    void login() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
        RefreshToken refreshToken = new RefreshToken(1L, "REFRESH-TOKEN");
        Long jwtExpirationInMilli = 900000L;
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword());
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        doNothing().when(securityContext).setAuthentication(authentication);
        when(jwtProvider.generateJwt(authentication)).thenReturn("JWT-TOKEN");
        when(refreshTokenService.generateRefreshToken()).thenReturn(refreshToken);
        when(jwtProvider.getJwtExpiration()).thenReturn(jwtExpirationInMilli);

        AuthenticationResponseDto actualAuthenticationResponseDto = authService.login(loginRequestDto);

        verify(authenticationManager, times(1)).authenticate(any(Authentication.class));
        verify(securityContext, times(1)).setAuthentication(any(Authentication.class));
        verify(jwtProvider, times(1)).generateJwt(any(Authentication.class));
        verify(refreshTokenService, times(1)).generateRefreshToken();
        verify(jwtProvider, times(1)).getJwtExpiration();

        assertThat(actualAuthenticationResponseDto).isNotNull();
        assertThat(actualAuthenticationResponseDto).isInstanceOf(AuthenticationResponseDto.class);
        assertThat(actualAuthenticationResponseDto.getAuthenticationToken()).isEqualTo("JWT-TOKEN");
        assertThat(actualAuthenticationResponseDto.getRefreshToken()).isEqualTo(refreshToken.getToken());
        assertThat(actualAuthenticationResponseDto.getUsername()).isEqualTo("username");
    }

    @Test
    void refreshToken() {
    }

    @Test
    void getCurrentUser() {
    }

    @Test
    void getUserFromToken() {
    }

    @Test
    void logout() {
    }

    @Test
    void checkIfUserExist() {
    }

    @Test
    void mapToResponseDto() {
    }
}
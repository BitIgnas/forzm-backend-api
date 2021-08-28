package org.forzm.demo.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.checkerframework.checker.nullness.Opt;
import org.forzm.demo.dto.*;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Collections;
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
    @Captor
    private ArgumentCaptor<String> refreshTokenArgumentCaptor;


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
        verifyNoMoreInteractions(authenticationManager);
        verifyNoMoreInteractions(refreshTokenService);
        verifyNoMoreInteractions(jwtProvider);

        assertThat(actualAuthenticationResponseDto).isNotNull();
        assertThat(actualAuthenticationResponseDto).isInstanceOf(AuthenticationResponseDto.class);
        assertThat(actualAuthenticationResponseDto.getAuthenticationToken()).isEqualTo("JWT-TOKEN");
        assertThat(actualAuthenticationResponseDto.getRefreshToken()).isEqualTo("REFRESH-TOKEN");
        assertThat(actualAuthenticationResponseDto.getRefreshToken()).isEqualTo(refreshToken.getToken());
        assertThat(actualAuthenticationResponseDto.getUsername()).isEqualTo("username");
    }

    @Test
    void refreshToken() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("REFRESH-TOKEN", "username");
        Long jwtExpirationInMilli = 900000L;

        doNothing().when(refreshTokenService).validateRefreshToken(anyString());
        when(jwtProvider.generateJwtWithUsername(anyString())).thenReturn("JWT-TOKEN");
        when(jwtProvider.getJwtExpiration()).thenReturn(jwtExpirationInMilli);

        AuthenticationResponseDto actualAuthenticationResponseDto = authService.refreshToken(refreshTokenRequestDto);

        verify(refreshTokenService, times(1)).validateRefreshToken(anyString());
        verify(jwtProvider, times(1)).generateJwtWithUsername(anyString());
        verify(jwtProvider, times(1)).getJwtExpiration();
        verifyNoMoreInteractions(refreshTokenService);
        verifyNoMoreInteractions(jwtProvider);

        assertThat(actualAuthenticationResponseDto).isNotNull();
        assertThat(actualAuthenticationResponseDto).isInstanceOf(AuthenticationResponseDto.class);
        assertThat(actualAuthenticationResponseDto.getAuthenticationToken()).isEqualTo("JWT-TOKEN");
        assertThat(actualAuthenticationResponseDto.getRefreshToken()).isEqualTo("REFRESH-TOKEN");
        assertThat(actualAuthenticationResponseDto.getUsername()).isEqualTo("username");
    }

    @Test
    void getCurrentUser() {
        User user = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test", "TEST", true, true, true, true, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userPrincipal);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User actualUser = authService.getCurrentUser();

        verify(securityContext, times(2)).getAuthentication();
        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(securityContext);
        verifyNoMoreInteractions(userRepository);

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).isInstanceOf(User.class);
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    void getUserFromToken() {
        User user = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        UserResponseDto userResponseDto = new UserResponseDto("test", "test@gmail.com", null, null);
        when(jwtProvider.getUsernameFromJwt(anyString())).thenReturn("test");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto actualUserResponseDto = authService.getUserFromToken("test");

        verify(jwtProvider, times(1)).getUsernameFromJwt(anyString());
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(jwtProvider);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualUserResponseDto).isNotNull();
        assertThat(actualUserResponseDto).isInstanceOf(UserResponseDto.class);
        assertThat(actualUserResponseDto).isEqualTo(userResponseDto);
    }

    @Test
    void logout() {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("REFRESH-TOKEN", "username");

        doNothing().when(refreshTokenRepository).deleteByToken(refreshTokenRequestDto.getRefreshToken());
        authService.logout(refreshTokenRequestDto);

        verify(refreshTokenRepository, times(1)).deleteByToken(refreshTokenArgumentCaptor.capture());
        verifyNoMoreInteractions(refreshTokenRepository);

        String refreshToken = refreshTokenArgumentCaptor.getValue();

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isEqualTo(refreshTokenRequestDto.getRefreshToken());
    }

    @Test
    void mapToResponseDto() {
        User user = new User(1L, "test", "test@gmail.com", "TEST", true, null, null);
        UserResponseDto userResponseDto = new UserResponseDto("test", "test@gmail.com", null, null);

        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(userResponseDto);

        UserResponseDto actualUserResponseDto = authService.mapToResponseDto(user);

        verify(modelMapper, times(1)).map(user, UserResponseDto.class);
        verifyNoMoreInteractions(modelMapper);

        assertThat(actualUserResponseDto).isNotNull();
        assertThat(actualUserResponseDto).isInstanceOf(UserResponseDto.class);
        assertThat(actualUserResponseDto).isEqualTo(userResponseDto);

    }
}
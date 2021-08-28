package org.forzm.demo.controller;

import org.forzm.demo.dto.*;
import org.forzm.demo.model.RefreshToken;
import org.forzm.demo.service.AuthService;
import org.forzm.demo.service.impl.AuthServiceImpl;
import org.forzm.demo.service.impl.ForumServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void register() throws Exception {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto("TEST", "TEST@gmail.com", "TEST");
        doNothing().when(authService).register(registerRequestDto);

        authService.register(registerRequestDto);

        mockMvc.perform(post("/api/auth/register")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{\"username\": \"TEST\", \"email\":\"TEST@gmail.com\", \"password\": \"TEST\"}"))
                .andExpect(status().isCreated());

        verify(authService, times(2)).register(any(RegisterRequestDto.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    void login() throws Exception {
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(
                "JWT-TOKEN", "REFRESH-TOKEN", Instant.now().plusMillis(900000L), "TEST");
        when(authService.login(any(LoginRequestDto.class))).thenReturn(authenticationResponseDto);

        mockMvc.perform(post("/api/auth/login")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{\"username\": \"TEST\", \"password\":\"TEST\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authenticationToken").value(authenticationResponseDto.getAuthenticationToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponseDto.getRefreshToken()))
                .andExpect(jsonPath("$.expiresAt").value(authenticationResponseDto.getExpiresAt().toString()))
                .andExpect(jsonPath("$.username").value(authenticationResponseDto.getUsername()));

        verify(authService, times(1)).login(any(LoginRequestDto.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    void refreshToken() throws Exception {
        RefreshToken refreshToken = new RefreshToken(1L, "REFRESH-TOKEN");
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto(refreshToken.getToken(), "TEST");
        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto(
                "JWT-TOKEN", refreshToken.getToken(), Instant.now().plusMillis(900000L), "TEST");
        when(authService.refreshToken(any(RefreshTokenRequestDto.class))).thenReturn(authenticationResponseDto);

        mockMvc.perform(post("/api/auth/token/refresh")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{\"refreshToken\":\"" + refreshTokenRequestDto.getRefreshToken() +
                        "\", \"username\":\"" + refreshTokenRequestDto.getUsername() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.authenticationToken").value(authenticationResponseDto.getAuthenticationToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponseDto.getRefreshToken()))
                .andExpect(jsonPath("$.expiresAt").value(authenticationResponseDto.getExpiresAt().toString()))
                .andExpect(jsonPath("$.username").value(authenticationResponseDto.getUsername()));

        verify(authService, times(1)).refreshToken(any(RefreshTokenRequestDto.class));
        verifyNoMoreInteractions(authService);
    }

    @Test
    void getCurrentUser() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto("TEST", "TEST@GMAIL.com", Instant.now(), "IMG_URL");
        when(authService.getUserFromToken(anyString())).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/auth/user/{authToken}", "JWT-TOKEN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userResponseDto.getUsername()))
                .andExpect(jsonPath("$.email").value(userResponseDto.getEmail()))
                .andExpect(jsonPath("$.dateCreated").value(userResponseDto.getDateCreated().toString()))
                .andExpect(jsonPath("$.profileImageUrl").value(userResponseDto.getProfileImageUrl()));

        verify(authService, times(1)).getUserFromToken(anyString());
        verifyNoMoreInteractions(authService);
    }

    @Test
    void logout() throws Exception {
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("REFRESH-TOKEN", "TEST");
        doNothing().when(authService).logout(refreshTokenRequestDto);

        mockMvc.perform(post("/api/auth/logout")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content("{\"refreshToken\":\"" + refreshTokenRequestDto.getRefreshToken() +
                        "\", \"username\":\"" + refreshTokenRequestDto.getUsername() + "\"}"))
                .andExpect(status().isOk());

        verify(authService, times(1)).logout(any(RefreshTokenRequestDto.class));
        verifyNoMoreInteractions(authService);

    }
}
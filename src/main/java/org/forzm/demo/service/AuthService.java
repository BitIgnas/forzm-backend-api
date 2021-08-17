package org.forzm.demo.service;

import org.forzm.demo.dto.*;
import org.forzm.demo.model.User;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthenticationResponse login(LoginRequest loginRequest);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(RefreshTokenRequest refreshTokenRequest);
    void checkIfUserExist(String username);
    User getCurrentUser();
    UserResponseDto getUserFromToken(String token);
    UserResponseDto mapToResponseDto(User user);
}

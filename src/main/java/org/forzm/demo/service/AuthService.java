package org.forzm.demo.service;

import org.forzm.demo.dto.*;
import org.forzm.demo.model.User;

public interface AuthService {
    void register(RegisterRequestDto registerRequestDto);
    AuthenticationResponseDto login(LoginRequestDto loginRequestDto);
    AuthenticationResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
    void logout(RefreshTokenRequestDto refreshTokenRequestDto);
    void checkIfUserExist(String username);
    User getCurrentUser();
    UserResponseDto getUserFromToken(String token);
    UserResponseDto mapToResponseDto(User user);
}

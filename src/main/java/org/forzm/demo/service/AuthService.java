package org.forzm.demo.service;

import org.forzm.demo.dto.AuthenticationResponse;
import org.forzm.demo.dto.LoginRequest;
import org.forzm.demo.dto.RefreshTokenRequest;
import org.forzm.demo.dto.RegisterRequest;
import org.forzm.demo.model.User;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthenticationResponse login(LoginRequest loginRequest);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(RefreshTokenRequest refreshTokenRequest);
    void checkIfUserExist(String username);
    User getCurrentUser();
}

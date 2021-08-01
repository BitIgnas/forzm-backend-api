package org.forzm.demo.service;

import org.forzm.demo.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken();
    void validateRefreshToken(String refreshToken);
    void deleteToken(String refreshToken);
}

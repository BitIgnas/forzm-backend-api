package org.forzm.demo.service.impl;

import lombok.AllArgsConstructor;
import org.forzm.demo.exception.RefreshTokenException;
import org.forzm.demo.model.RefreshToken;
import org.forzm.demo.repository.RefreshTokenRepository;
import org.forzm.demo.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String refreshToken) {
        refreshTokenRepository.findRefreshTokenByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh token was not found"));
    }

    @Override
    @Transactional
    public void deleteToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}

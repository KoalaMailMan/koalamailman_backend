package com.koa.koalamailman.domain.auth.service;

import com.koa.koalamailman.domain.auth.repository.RefreshTokenRepository;
import com.koa.koalamailman.domain.auth.repository.entity.RefreshToken;
import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.global.exception.BaseException;
import com.koa.koalamailman.global.exception.error.AuthErrorCode;
import com.koa.koalamailman.global.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.apache.commons.codec.digest.HmacUtils.hmacSha256;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh.secret}")
    private String secretKey;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationMs;

    @Transactional
    public String validateAndGenerateAccessToken(User user, String rawRefreshToken) {
        RefreshToken refreshToken = findByUserId(user.getId());

        byte[] incomingHash = hmacSha256(secretKey, rawRefreshToken);

        if (!MessageDigest.isEqual(refreshToken.getTokenHash(), incomingHash)) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED);
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED);
        }

        return jwtProvider.generateToken(user, refreshTokenExpirationMs);
    }
    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = jwtProvider.generateToken(user, refreshTokenExpirationMs);
        byte[] tokenHash = hmacSha256(secretKey, rawToken);
        LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(refreshTokenExpirationMs));

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElse(RefreshToken.create(user.getId(),  tokenHash, expiresAt));
        refreshToken.updateToken(tokenHash, expiresAt);

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Transactional
    public void extendRefreshToken(User user, byte[] tokenHash) {
        RefreshToken refreshToken = findByUserId(user.getId());
        refreshToken.updateToken(tokenHash, LocalDateTime.now().plus(Duration.ofMillis(refreshTokenExpirationMs)));
    }

    private RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BaseException(AuthErrorCode.UNAUTHORIZED));
    }
}

package com.koa.koalamailman.domain.auth.service;

import com.koa.koalamailman.domain.auth.repository.RefreshTokenRepository;
import com.koa.koalamailman.domain.auth.repository.entity.RefreshToken;
import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.global.exception.BusinessException;
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
    public String validateAndGenerateAccessToken(String rawRefreshToken) {
        Long userId = Long.parseLong(jwtProvider.getSubjectFromToken(rawRefreshToken));
        RefreshToken refreshToken = findByUserId(userId);

        byte[] incomingHash = hmacSha256(secretKey, rawRefreshToken);

        if (!MessageDigest.isEqual(refreshToken.getTokenHash(), incomingHash)) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }

        return jwtProvider.generateToken(userId, null, refreshTokenExpirationMs);
    }
    @Transactional
    public String createRefreshToken(User user) {
        String rawToken = jwtProvider.generateToken(user.getId(), null, refreshTokenExpirationMs);
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
                .orElseThrow(() -> new BusinessException(AuthErrorCode.UNAUTHORIZED));
    }
}

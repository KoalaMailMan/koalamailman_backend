package com.koa.koalamailman.domain.auth.service;

import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.global.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    private final JwtProvider jwtProvider;
    @Value("${jwt.access.expiration}")
    private long accessExpirationTimeMs;

    public String createAccessToken(User user) {
        return jwtProvider.generateToken(user.getId(), user.getEmail(), accessExpirationTimeMs);
    }
}

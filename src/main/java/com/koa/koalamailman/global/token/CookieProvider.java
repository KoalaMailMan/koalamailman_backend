package com.koa.koalamailman.global.token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieProvider {
    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;
    public ResponseCookie setRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenExpiration))
                .sameSite("STRICT")
                .build();
    }
}
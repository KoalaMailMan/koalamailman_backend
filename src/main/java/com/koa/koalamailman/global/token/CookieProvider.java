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
                .secure(false) //개발 환경에서 잠시 false로 추후 https 적용 시 true
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenExpiration))
                .sameSite("NONE")
                .build();
    }
}
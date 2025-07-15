package com.koa.RingDong.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("email", user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        String userId = getSubjectFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userId));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.warn("[JWT 검증 실패] 만료된 토큰: {}", e.getMessage());
        } catch (AlgorithmMismatchException e) {
            log.warn("[JWT 검증 실패] 알고리즘 불일치: {}", e.getMessage());
        } catch (SignatureVerificationException e) {
            log.warn("[JWT 검증 실패] 서명 검증 실패: {}", e.getMessage());
        } catch (JWTDecodeException e) {
            log.warn("[JWT 검증 실패] 디코딩 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("[JWT 검증 실패] 기타 오류: {}", e.getMessage());
        }
        return false;
    }
}


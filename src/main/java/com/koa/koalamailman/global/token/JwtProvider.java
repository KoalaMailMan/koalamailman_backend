package com.koa.koalamailman.global.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.global.security.oauth.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.access.secret}")
    private String secretKey;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String generateToken(Long userId, String email, long expirationTimeMs) {
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMs))
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String userId = getSubjectFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
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

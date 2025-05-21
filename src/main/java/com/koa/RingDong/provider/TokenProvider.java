package com.koa.RingDong.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateAccessToken(String kakaoId) {
        return JWT.create()
                .withSubject(kakaoId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token)
                .getSubject();
    }

    public Authentication getAuthentication(String token) {
        String kakaoId = getSubjectFromToken(token); // JWT에서 sub 추출

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails userDetails = new User(kakaoId, "", authorities); // Spring Security User 객체 사용

        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


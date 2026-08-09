package com.koa.koalamailman.global.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.koa.koalamailman.global.security.oauth.CustomUserDetailsService;
import com.koa.koalamailman.global.token.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectProvider<JwtDecoder> jwtDecoderProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (StringUtils.hasText(token)) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        if (isLegacyAccessToken(token) && tokenProvider.validateToken(token)) {
            return tokenProvider.getAuthentication(token);
        }

        JwtDecoder jwtDecoder = jwtDecoderProvider.getIfAvailable();
        if (jwtDecoder == null) {
            return null;
        }

        try {
            org.springframework.security.oauth2.jwt.Jwt jwt = jwtDecoder.decode(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
            return new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
        } catch (JwtException e) {
            log.warn("[OAuth2 JWT 검증 실패] {}", e.getMessage());
            return null;
        }
    }

    private boolean isLegacyAccessToken(String token) {
        try {
            return "HS256".equals(JWT.decode(token).getAlgorithm());
        } catch (JWTDecodeException e) {
            return false;
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}

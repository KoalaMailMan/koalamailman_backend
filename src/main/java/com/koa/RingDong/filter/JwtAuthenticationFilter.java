package com.koa.RingDong.filter;

import com.koa.RingDong.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        log.debug("[JWT 필터] 요청 URI: {}, 토큰 존재: {}", request.getRequestURI(), StringUtils.hasText(token));

        if (StringUtils.hasText(token)) {
            if (tokenProvider.validateToken(token)) {
                try {
                    Authentication auth = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.debug("[JWT 필터] 인증 성공 - URI: {}", request.getRequestURI());
                } catch (Exception e) {
                    log.error("[JWT 필터] 인증 실패 - URI: {}, 에러: {}", request.getRequestURI(), e.getMessage());
                    SecurityContextHolder.clearContext();
                }
            } else {
                log.warn("[JWT 필터] 유효하지 않은 토큰 - URI: {}", request.getRequestURI());
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        Optional<Cookie> tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst();

        return tokenCookie.map(Cookie::getValue).orElse(null);
    }
}

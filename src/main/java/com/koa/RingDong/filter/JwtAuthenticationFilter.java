package com.koa.RingDong.filter;

import com.koa.RingDong.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService tokenService;
    private final String header = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        getTokenString(request.getHeader(header))
                .filter(StringUtils::hasText)
                .ifPresent(token -> {
                    if (tokenService.validateToken(token)) {
                        try {
                            Authentication auth = tokenService.getAuthentication(token);
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
                });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenString(String header) {
        if (header == null) {
            return Optional.empty();
        } else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }
}

package com.koa.RingDong.global.security.handler;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import com.koa.RingDong.domain.user.repository.User;
import com.koa.RingDong.domain.user.service.UserService;
import com.koa.RingDong.global.token.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider tokenService;
    private final UserService userService;

    @Value("${app.oauth2.front-uri}")
    private String frontUri;
    @Value("${app.oauth2.domain}")
    private String cookieDomain;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());

        Map<String, Object> attrs = oAuth2User.getAttributes();
        String providerId = String.valueOf(attrs.get("providerId")); // 또는 google은 보통 "sub"
        String name       = (String) attrs.get("name");
        String email      = (String) attrs.get("email");


        User user = userService.findOrCreate(provider, providerId, name, email);
        String jwtAccessToken = tokenService.generateAccessToken(user);
        log.info("[JWT 생성] userId: {}, token: {}", user.getId(), jwtAccessToken);

        ResponseCookie cookie = ResponseCookie.from("access_token", jwtAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("STRICT")
                .domain(cookieDomain)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        response.sendRedirect(frontUri + "/dashboard");
    }
}


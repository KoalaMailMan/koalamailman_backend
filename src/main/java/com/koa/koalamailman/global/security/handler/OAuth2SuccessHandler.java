package com.koa.koalamailman.global.security.handler;

import com.koa.koalamailman.domain.user.repository.OAuthProvider;
import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.domain.user.service.UserService;
import com.koa.koalamailman.global.token.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
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
        String providerId = String.valueOf(attrs.get("providerId"));
        String name       = (String) attrs.get("name");
        String email      = (String) attrs.get("email");


        User user = userService.findOrCreate(provider, providerId, name, email);
        String accessToken = tokenService.generateAccessToken(user);

        String targetUrl = UriComponentsBuilder
                .fromHttpUrl(frontUri)
                .queryParam("access_token", accessToken)
                .build().toUriString();

        response.sendRedirect(targetUrl);
    }
}


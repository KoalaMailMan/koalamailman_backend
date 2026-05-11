package com.koa.koalamailman.global.security.handler;

import com.koa.koalamailman.user.application.UserAuthUseCase;
import com.koa.koalamailman.auth.application.RefreshTokenService;
import com.koa.koalamailman.user.domain.OAuthProvider;
import com.koa.koalamailman.user.domain.User;
import com.koa.koalamailman.global.token.CookieProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final CookieProvider cookieProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserAuthUseCase userAuthUseCase;

    @Value("${app.oauth2.login-redirect-uri}")
    private String loginRedirectUri;
    @Value("${app.oauth2.domain}")
    private String cookieDomain;
    @Value("${app.oauth2.local-redirect-uri}")
    private String localRedirectUri;
    @Value("${app.oauth2.local-test-emails:#{null}}")
    private List<String> localTestEmails;

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


        User user = userAuthUseCase.findOrCreate(provider, providerId, name, email);

        String refreshToken = refreshTokenService.createRefreshToken(user);

        ResponseCookie cookie = cookieProvider.setRefreshTokenCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        boolean isLocalTest = localTestEmails != null && localTestEmails.contains(email);
        String targetUrl = UriComponentsBuilder
                .fromHttpUrl(isLocalTest ? localRedirectUri : loginRedirectUri)
                .build().toUriString();

        response.sendRedirect(targetUrl);
    }
}


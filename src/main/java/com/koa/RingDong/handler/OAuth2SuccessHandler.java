package com.koa.RingDong.handler;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.provider.TokenProvider;
import com.koa.RingDong.repository.UserRepository;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Value("${app.oauth2.front-uri}")
    private String frontUri;
    @Value("${app.oauth2.domain}")
    private String cookieDomain;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. 인증된 사용자 정보에서 attribute 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());

        // 2. oauthId 추출
        String oauthId = oAuth2User.getAttribute("id").toString();

        // 3. DB에서 유저 조회
        User user = userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 4. JWT access token 생성
        String jwtAccessToken = tokenProvider.generateAccessToken(user.getId());
        log.info("[JWT 생성] userId: {}, token: {}", user.getId(), jwtAccessToken);

        // 5. JWT를 HttpOnly Secure 쿠키에 담아 전달
        ResponseCookie cookie = ResponseCookie.from("token", jwtAccessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("None")
                .domain(cookieDomain)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 6. 프론트엔드로 리다이렉트
        response.sendRedirect(frontUri + "/dashboard");
    }
}


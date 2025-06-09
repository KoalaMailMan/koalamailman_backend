package com.koa.RingDong.handler;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.provider.TokenProvider;
import com.koa.RingDong.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. 인증된 사용자 정보에서 attribute 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        // 2. oauthId 추출
        String oauthId = oAuth2User.getAttribute("id").toString();

        // 3. DB에서 유저 조회
        User user = userRepository.findByOauthIdAndOauthProvider(
                oauthId,
                OAuthProvider.valueOf(registrationId.toUpperCase())
        ).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 4. JWT access token 생성
        String jwtAccessToken = tokenProvider.generateAccessToken(user.getId());
        log.info("[JWT 생성] userId: {}, token: {}", user.getId(), jwtAccessToken);

        // 5. acces token 프론트에 응답
        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}


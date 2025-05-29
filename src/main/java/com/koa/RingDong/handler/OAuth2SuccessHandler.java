package com.koa.RingDong.handler;

import com.koa.RingDong.entity.User;
import com.koa.RingDong.provider.TokenProvider;
import com.koa.RingDong.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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


        // 인증된 사용자 정보에서 id 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String kakaoId = oAuth2User.getAttribute("id").toString();
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // JWT access token 생성
        String jwtAccessToken = tokenProvider.generateAccessToken(user.getId());
        // (필요시 refreshToken도 생성)

        log.info("[JWT 생성] userId: {}, token: {}", user.getId(), jwtAccessToken);

        // access token 프론트에 응답
        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

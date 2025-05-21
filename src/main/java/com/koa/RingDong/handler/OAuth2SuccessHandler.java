package com.koa.RingDong.handler;

import com.koa.RingDong.provider.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {


        // 인증된 사용자 정보에서 이메일 or kakaoId 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String kakaoId = oAuth2User.getAttribute("id").toString();

        // JWT access token 생성
        String jwtAccessToken = tokenProvider.generateAccessToken(kakaoId);
        // (필요시 refreshToken도 생성)

        // access token 프론트에 응답
        response.setHeader("Authorization", "Bearer " + jwtAccessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

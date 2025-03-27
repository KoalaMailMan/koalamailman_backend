package com.koa.RingDong.controller;


import com.koa.RingDong.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<?> loginSuccess(OAuth2AuthenticationToken authenticationToken) {

        // Kakao user info
        Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();

        String kakaoId = String.valueOf(attributes.get("id"));
        String nickname = ((Map<String, Object>) attributes.get("properties")).get("nickname").toString();

        // JWT
        String token = jwtUtil.createToken(kakaoId);

        return ResponseEntity.ok(Map.of(
                "kakaoId", kakaoId,
                "nickname", nickname,
                "token", token
        ));
    }
}

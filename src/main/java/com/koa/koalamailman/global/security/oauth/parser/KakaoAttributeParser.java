package com.koa.koalamailman.global.security.oauth.parser;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoAttributeParser implements OauthAttributeParser {

    @Override
    public Map<String, Object> parse(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return Map.of(
                "providerId", String.valueOf(attributes.get("id")),
                "email", kakaoAccount.get("email"),
                "name", profile.get("nickname"),
                "provider", "google"
        );
    }
}
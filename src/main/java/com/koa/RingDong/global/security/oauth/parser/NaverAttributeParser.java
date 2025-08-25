package com.koa.RingDong.global.security.oauth.parser;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverAttributeParser implements OauthAttributeParser {

    @Override
    public Map<String, Object> parse(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return Map.of(
                "providerId", (String) response.get("id"),
                "email", (String) response.get("email"),
                "name", (String) response.get("name"),
                "provider", "naver"
        );
    }
}

package com.koa.koalamailman.global.security.oauth.parser;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleAttributeParser implements OauthAttributeParser {

    @Override
    public Map<String, Object> parse(OAuth2User oAuth2User) {
        Map<String, Object> original = oAuth2User.getAttributes();

        return Map.of(
                "providerId", original.get("sub"),
                "email", original.get("email"),
                "name", original.get("name"),
                "provider", "google"
        );
    }
}
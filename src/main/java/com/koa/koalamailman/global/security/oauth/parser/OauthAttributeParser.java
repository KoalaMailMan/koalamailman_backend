package com.koa.koalamailman.global.security.oauth.parser;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface OauthAttributeParser {
    Map<String, Object> parse(OAuth2User oAuth2User);
}
package com.koa.RingDong.global.security.oauth.parser;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleAttributeParser implements OauthAttributeParser {

    @Override
    public OauthUserInfo parse(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        return OauthUserInfo.builder()
                .loginType(OAuthProvider.GOOGLE)
                .oauthId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }
}
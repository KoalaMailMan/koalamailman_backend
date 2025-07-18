package com.koa.RingDong.global.security.oauth.parser;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class NaverAttributeParser implements OauthAttributeParser {

    @Override
    public OauthUserInfo parse(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OauthUserInfo.builder()
                .loginType(OAuthProvider.NAVER)
                .oauthId((String) response.get("id"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .build();
    }
}

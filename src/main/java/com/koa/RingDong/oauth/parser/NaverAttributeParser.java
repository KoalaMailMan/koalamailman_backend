package com.koa.RingDong.oauth.parser;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.oauth.OauthUserInfo;

import java.util.Map;

public class NaverAttributeParser implements OauthAttributeParser {

    @Override
    public OauthUserInfo parse(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OauthUserInfo.builder()
                .loginType(OAuthProvider.NAVER)
                .oauthId((String) response.get("id"))
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .build();
    }
}

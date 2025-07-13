package com.koa.RingDong.oauth.parser;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.oauth.OauthUserInfo;

import java.util.Map;

public class GoogleAttributeParser implements OauthAttributeParser {

    @Override
    public OauthUserInfo parse(Map<String, Object> attributes) {
        return OauthUserInfo.builder()
                .loginType(OAuthProvider.GOOGLE)
                .oauthId((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .build();
    }
}
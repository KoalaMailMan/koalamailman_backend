package com.koa.RingDong.global.security.oauth.parser;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OauthUserInfo {
    private final OAuthProvider loginType;
    private final String oauthId;
    private final String name;
    private final String email;

}
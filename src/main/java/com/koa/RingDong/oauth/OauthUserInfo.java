package com.koa.RingDong.oauth;

import com.koa.RingDong.entity.OAuthProvider;
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
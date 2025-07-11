package com.koa.RingDong.oauth.parser;

import com.koa.RingDong.oauth.OauthUserInfo;

import java.util.Map;

public interface OauthAttributeParser {
    OauthUserInfo parse(Map<String, Object> attributes);
}
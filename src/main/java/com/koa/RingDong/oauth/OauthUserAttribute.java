package com.koa.RingDong.oauth;

import com.koa.RingDong.oauth.parser.OauthAttributeParser;

import java.util.Map;

public class OauthUserAttribute {
    private final Map<String, Object> attributes;
    private final OauthAttributeParser parser;

    public OauthUserAttribute(Map<String, Object> attributes, OauthAttributeParser parser) {
        this.attributes = attributes;
        this.parser = parser;
    }

    public OauthUserInfo toOauthUserInfo() {
        return parser.parse(attributes);
    }
}


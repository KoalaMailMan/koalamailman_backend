package com.koa.RingDong.global.security.oauth.parser;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class OauthAttributeParserFactory {

    private final Map<OAuthProvider, OauthAttributeParser> parserMap = new EnumMap<>(OAuthProvider.class);

    public OauthAttributeParserFactory() {
        parserMap.put(OAuthProvider.GOOGLE, new GoogleAttributeParser());
        parserMap.put(OAuthProvider.NAVER, new NaverAttributeParser());
    }

    public OauthAttributeParser getParser(OAuthProvider provider) {
        if (!parserMap.containsKey(provider)) {
            throw new IllegalArgumentException("Unsupported OAuth Provider: " + provider);
        }
        return parserMap.get(provider);
    }
}
package com.koa.RingDong.global.security.oauth.parser;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OauthAttributeParserFactory {

    private static final Map<OAuthProvider, OauthAttributeParser> parserMap = Map.of(
            OAuthProvider.GOOGLE, new GoogleAttributeParser(),
            OAuthProvider.NAVER, new NaverAttributeParser(),
            OAuthProvider.KAKAO, new KakaoAttributeParser()
    );

    public static Map<String, Object> parse(OAuthProvider provider, OAuth2User oAuth2User) {
        OauthAttributeParser parser = parserMap.get(provider);

        if (parser == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth Provider: " + provider);
        }
        return parser.parse(oAuth2User);
    }
}
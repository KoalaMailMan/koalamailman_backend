package com.koa.RingDong.global.security.oauth;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import com.koa.RingDong.domain.user.repository.User;
import com.koa.RingDong.global.security.oauth.parser.OauthUserAttribute;
import com.koa.RingDong.global.security.oauth.parser.OauthUserInfo;
import com.koa.RingDong.global.security.oauth.parser.OauthAttributeParser;
import com.koa.RingDong.global.security.oauth.parser.OauthAttributeParserFactory;
import com.koa.RingDong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OauthAttributeParserFactory parserFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthProvider provider = OAuthProvider.valueOf(registrationId.toUpperCase());

        OauthAttributeParser parser = parserFactory.getParser(provider);
        OauthUserAttribute attribute = new OauthUserAttribute(oAuth2User, parser);
        OauthUserInfo userInfo = attribute.toOauthUserInfo();

        userRepository.findByOauthIdAndOauthProvider(userInfo.getOauthId(), provider)
            .orElseGet(() -> userRepository.save(
                    User.builder()
                            .oauthId(userInfo.getOauthId())
                            .oauthProvider(provider)
                            .nickname(userInfo.getName())
                            .email(userInfo.getEmail())
                            .build()
            ));

        Map<String, Object> customAttributes = Map.of(
                "id", userInfo.getOauthId(),
                "nickname", userInfo.getName(),
                "email", userInfo.getEmail()
        );

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "id"
        );
    }
}
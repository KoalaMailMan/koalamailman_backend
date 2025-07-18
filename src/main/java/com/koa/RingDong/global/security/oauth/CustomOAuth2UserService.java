package com.koa.RingDong.global.security.oauth;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import com.koa.RingDong.domain.user.repository.User;
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

        Map<String, Object> customAttributes = OauthAttributeParserFactory.parse(provider, oAuth2User);

        userRepository.findByOauthIdAndOauthProvider((String) customAttributes.get("id"), provider)
            .orElseGet(() -> userRepository.save(
                    User.builder()
                            .oauthId((String) customAttributes.get("id"))
                            .oauthProvider(provider)
                            .nickname((String) customAttributes.get("name"))
                            .email((String) customAttributes.get("email"))
                            .build()
            ));

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "id"
        );
    }
}
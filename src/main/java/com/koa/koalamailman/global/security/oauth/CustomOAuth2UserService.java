package com.koa.koalamailman.global.security.oauth;

import com.koa.koalamailman.auth.application.AuthUseCase;
import com.koa.koalamailman.user.domain.OAuthProvider;
import com.koa.koalamailman.user.application.UserUseCase;
import com.koa.koalamailman.global.security.oauth.parser.OauthAttributeParserFactory;
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

    private final AuthUseCase authUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuthProvider provider = OAuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        Map<String, Object> customAttributes = OauthAttributeParserFactory.parse(provider, oAuth2User);

        authUseCase.findOrCreate(provider, (String) customAttributes.get("providerId"), (String) customAttributes.get("name"), (String) customAttributes.get("email"));

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("USER")),
                customAttributes,
                "providerId"
        );
    }
}
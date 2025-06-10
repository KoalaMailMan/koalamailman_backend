package com.koa.RingDong.service;

import com.koa.RingDong.entity.OAuthProvider;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String oauthId;
        String nickname;
        String email;

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            oauthId = attributes.get("id").toString();
            nickname = (String) profile.get("nickname");
            email = (String) kakaoAccount.get("email");

        } else if ("naver".equals(registrationId)) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            oauthId = (String) response.get("id");
            nickname = (String) response.get("name");
            email = (String) response.get("email");

        } else if ("google".equals(registrationId)) {

            oauthId = (String) attributes.get("sub");
            nickname = (String) attributes.get("name");
            email = (String) attributes.get("email");

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 로그인입니다.");
        }

        User user = userRepository.findByOauthIdAndOauthProvider(oauthId, OAuthProvider.valueOf(registrationId.toUpperCase()))
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .oauthId(oauthId)
                            .oauthProvider(OAuthProvider.valueOf(registrationId.toUpperCase()))
                            .nickname(nickname)
                            .email(email)
                            .build();
                    return userRepository.save(newUser);
                });

        Map<String, Object> customAttributes = Map.of(
                "id", oauthId,
                "nickname", nickname,
                "email", email
        );

        return new DefaultOAuth2User(
                Set.of(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "id"
        );
    }
}
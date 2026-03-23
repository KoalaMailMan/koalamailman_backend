package com.koa.koalamailman.user.application;

import com.koa.koalamailman.user.domain.OAuthProvider;
import com.koa.koalamailman.user.domain.User;
import com.koa.koalamailman.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthUseCase {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreate(OAuthProvider provider, String oauthId, String name, String email) {

        return userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .oauthProvider(provider)
                                .oauthId(oauthId)
                                .nickname(name)
                                .email(email)
                                .build()
                ));
    }

}

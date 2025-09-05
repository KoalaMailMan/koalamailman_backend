package com.koa.koalamailman.domain.user.service;

import com.koa.koalamailman.domain.user.repository.*;
import com.koa.koalamailman.domain.user.dto.UserResponse;
import com.koa.koalamailman.global.exception.error.UserErrorCode;
import com.koa.koalamailman.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

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

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public User getUserByOauthInfo(OAuthProvider provider, String providerId) {
        return userRepository.findByOauthIdAndOauthProvider(providerId, provider)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserProfile(Long userId, AgeGroup ageGroup, Gender gender, String job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
        user.updateProfile(gender, ageGroup, job);
    }
}

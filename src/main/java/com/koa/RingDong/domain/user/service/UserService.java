package com.koa.RingDong.domain.user.service;

import com.koa.RingDong.domain.user.repository.*;
import com.koa.RingDong.domain.user.dto.UserResponse;
import com.koa.RingDong.global.exception.ErrorCode;
import com.koa.RingDong.global.exception.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User findOrCreate(OAuthProvider provider, String providerId, String name, String email) {

        return userRepository.findByProviderIdAndOauthProvider(providerId, provider)
            .orElseGet(() -> userRepository.save(
                    User.builder()
                            .oauthProvider(provider)
                            .providerId(providerId)
                            .nickname(name)
                            .email(email)
                            .build()
            ));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.of(user);
    }

    @Transactional(readOnly = true)
    public User getUserByOauthInfo(OAuthProvider provider, String providerId) {
        return userRepository.findByProviderIdAndOauthProvider(providerId, provider)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserProfile(Long userId, AgeGroup ageGroup, Gender gender, String job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.updateProfile(gender, ageGroup, job);
    }
}

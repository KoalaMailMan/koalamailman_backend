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
    public void findOrCreateFromOAuth(OAuthProvider provider, Map<String, Object> customAttributes) {
        String oauthId = (String) customAttributes.get("id");

        userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
            .orElseGet(() -> userRepository.save(
                    User.builder()
                            .oauthId(oauthId)
                            .oauthProvider(provider)
                            .nickname((String) customAttributes.get("name"))
                            .email((String) customAttributes.get("email"))
                            .build()
            ));
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserResponse getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.of(user.getNickname(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserByOauthInfo(OAuthProvider provider, String oauthId) {
        return userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserProfile(Long userId, AgeGroup ageGroup, Gender gender, String job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        user.updateProfile(gender, ageGroup, job);
    }
}

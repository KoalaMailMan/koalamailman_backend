package com.koa.RingDong.domain.user.service;

import com.koa.RingDong.domain.user.repository.OAuthProvider;
import com.koa.RingDong.domain.user.repository.User;
import com.koa.RingDong.domain.user.dto.UserResponse;
import com.koa.RingDong.domain.user.repository.UserRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return UserResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public User getUserByOauthInfo(OAuthProvider provider, String oauthId) {
        return userRepository.findByOauthIdAndOauthProvider(oauthId, provider)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public UserResponse updateUserNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.updateNickname(nickname);

        return UserResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public void updateUserProfile(Long userId, String ageGroup, String gender, String job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.updateProfile(ageGroup, gender, job);
    }
}

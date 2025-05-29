package com.koa.RingDong.service;

import com.koa.RingDong.dto.response.UserResponse;
import com.koa.RingDong.entity.User;
import com.koa.RingDong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public UserResponse getUserInfo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return UserResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public UserResponse updateUserNickname(Long userId, String nickname){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setNickname(nickname);

        return UserResponse.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

}

package com.koa.RingDong.service;

import com.koa.RingDong.entity.User;
import com.koa.RingDong.repository.UserRepository;
import com.koa.RingDong.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdString) throws UsernameNotFoundException {
        Long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("잘못된 사용자 ID 형식");
        }

        System.out.println("JWT에서 추출한 userId: " + userId);
        System.out.println("DB에 존재하는 userIds: " + userRepository.findAll().stream().map(User::getId).toList());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(user);
    }
}

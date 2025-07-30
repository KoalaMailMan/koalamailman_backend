package com.koa.RingDong.global.security.oauth;

import com.koa.RingDong.domain.user.repository.User;
import com.koa.RingDong.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdString) throws UsernameNotFoundException {
        Long userId;
        try {
            userId = Long.parseLong(userIdString);
            log.info("[JWT 인증] 토큰에서 추출한 userId: {}", userId);
        } catch (NumberFormatException e) {
            log.error("[JWT 인증] 잘못된 userId 형식: {}", userIdString);
            throw new UsernameNotFoundException("잘못된 사용자 ID 형식");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[JWT 인증] 사용자를 찾을 수 없음 - userId: {}", userId);
                    return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
                });

        log.info("[JWT 인증] 사용자 인증 성공 - userId: {}, email: {}", user.getId(), user.getEmail());
        return new CustomUserDetails(user);
    }
}

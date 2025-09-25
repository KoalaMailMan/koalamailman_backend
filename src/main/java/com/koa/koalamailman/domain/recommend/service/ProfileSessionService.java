package com.koa.koalamailman.domain.recommend.service;

import com.koa.koalamailman.domain.recommend.session.ProfileSession;
import com.koa.koalamailman.domain.user.repository.User;
import com.koa.koalamailman.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileSessionService {

    private final UserRepository userRepository;

    public void initProfileToSession(Long userId, HttpSession session) {

        // conversation UUID 생성
        UUID conversationId = UUID.randomUUID();

        // User profile 정보 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        log.info("DBG profile payload - ageGroup={}, job={}, gender={}",
                user.getAgeGroup(), user.getJob(), user.getGender());

        ProfileSession profileSession = ProfileSession.builder()
                .conversationId(conversationId)
                .ageGroup(user.getAgeGroup())
                .job(user.getJob())
                .gender(user.getGender())
                .build();

        session.setAttribute("session:profile:", profileSession);
    }
}

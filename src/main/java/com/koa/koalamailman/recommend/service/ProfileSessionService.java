package com.koa.koalamailman.recommend.service;

import com.koa.koalamailman.recommend.session.ProfileSession;
import com.koa.koalamailman.user.infrastructure.UserRepository;
import com.koa.koalamailman.global.exception.BusinessException;
import com.koa.koalamailman.global.exception.error.UserErrorCode;
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
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));

        log.info("DBG profile payload - ageGroup={}, job={}, gender={}",
                user.getAgeGroup(), user.getJob(), user.getGender());

        ProfileSession profileSession = ProfileSession.builder()
                .conversationId(conversationId)
                .ageGroup(user.getAgeGroup())
                .job(user.getJob())
                .gender(user.getGender())
                .build();

        session.setAttribute(ProfileSession.SESSION_KEY, profileSession);
    }
}

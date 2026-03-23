package com.koa.koalamailman.user.application;

import com.koa.koalamailman.user.domain.AgeGroup;
import com.koa.koalamailman.user.domain.Gender;
import com.koa.koalamailman.user.domain.User;
import com.koa.koalamailman.user.infrastructure.UserRepository;
import com.koa.koalamailman.global.exception.error.UserErrorCode;
import com.koa.koalamailman.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public User getUserById(Long userId){
        return findUserById(userId);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateUserProfile(Long userId, AgeGroup ageGroup, Gender gender, String job) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
        user.updateProfile(gender, ageGroup, job);
    }
}

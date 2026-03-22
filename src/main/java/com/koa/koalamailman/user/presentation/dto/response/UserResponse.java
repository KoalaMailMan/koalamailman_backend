package com.koa.koalamailman.user.presentation.dto.response;

import com.koa.koalamailman.user.domain.User;

public record UserResponse (String nickname, String email) {

    public static UserResponse from(
            final User user
    ) {
        return new UserResponse(user.getNickname(), user.getEmail());
    }
}

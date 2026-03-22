package com.koa.koalamailman.user.dto;

import com.koa.koalamailman.user.repository.User;

public record UserResponse (String nickname, String email) {

    public static UserResponse of(
            final User user
    ) {
        return new UserResponse(user.getNickname(), user.getEmail());
    }
}

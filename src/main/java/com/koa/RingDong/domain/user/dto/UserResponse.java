package com.koa.RingDong.domain.user.dto;

public record UserResponse (String nickname, String email) {

    public static UserResponse of(
            final String nickname,
            final String email
    ) {
        return new UserResponse(nickname, email);
    }
}

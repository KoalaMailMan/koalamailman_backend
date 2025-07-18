package com.koa.RingDong.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    private String nickname;
    private String email;
}

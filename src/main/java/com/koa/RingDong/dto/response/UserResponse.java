package com.koa.RingDong.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponse {

    private Long id;
    private String nickname;
    private String email;
}

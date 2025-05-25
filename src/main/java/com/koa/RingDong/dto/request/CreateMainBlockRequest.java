package com.koa.RingDong.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMainBlockRequest {
    @NotNull(message = "userId는 필수입니다.")
    private Long userId;

    private String content;
}

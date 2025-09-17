package com.koa.koalamailman.domain.mandalart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCoreGoalRequest(
        Long goalId,
        @Size(max = 40, message = "최대 입력 길이(40자)를 초과했습니다.")
        @Schema(description = "Core goal 내용")
        String content,

        @Size(min = 0, max = 8)
        @Valid
        List<UpdateMainGoalRequest> mains
) {
}

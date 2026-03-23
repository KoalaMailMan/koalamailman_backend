package com.koa.koalamailman.mandalart.presentation.dto.request;

import com.koa.koalamailman.mandalart.application.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.application.dto.MainGoalDto;
import com.koa.koalamailman.mandalart.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateCoreGoalRequest(
        @Schema(description = "create 시에는 X")
        Long goalId,

        @Size(max = 40, message = "최대 입력 길이(40자)를 초과했습니다.")
        @Schema(description = "Core goal 내용")
        String content,

        @Schema(description = "Core goal status")
        Status status,

        @Size(max = 8, message = "main 목표는 최대 8개입니다.")
        @Valid
        List<UpdateMainGoalRequest> mains
) {

    public CoreGoalDto coreGoalDto() {
        List<MainGoalDto> mainDtos = (mains == null) ? List.of()
                : mains.stream()
                .map(UpdateMainGoalRequest::mainGoalDto)
                .toList();
        return new CoreGoalDto(goalId, content, status, mainDtos);
    }
}

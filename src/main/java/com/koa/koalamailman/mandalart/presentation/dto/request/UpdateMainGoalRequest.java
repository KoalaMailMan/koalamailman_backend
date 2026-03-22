package com.koa.koalamailman.mandalart.presentation.dto.request;

import com.koa.koalamailman.mandalart.application.dto.MainGoalDto;
import com.koa.koalamailman.mandalart.application.dto.SubGoalDto;
import com.koa.koalamailman.mandalart.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateMainGoalRequest (
    @Schema(description = "create 시에는 X")
    Long goalId,

    @NotNull
    @Min(1) @Max(8)
    Integer position,

    @Size(max = 40, message = "최대 입력 길이(40자)를 초과했습니다.")
    @Schema(description = "main goal 내용")
    String content,

    @Schema(description = "main goal status")
    Status status,

    @Size(max = 8, message = "main 별 sub는 최대 8개입니다.")
    @Valid
    List<UpdateSubGoalRequest> subs
) {
    public MainGoalDto mainGoalDto() {
        List< SubGoalDto> subDtos = (subs == null) ? List.of()
                :subs.stream()
                .map(UpdateSubGoalRequest::subGoalDto)
                .toList();
        return new MainGoalDto(goalId, position, content, status, subDtos);
    }
}

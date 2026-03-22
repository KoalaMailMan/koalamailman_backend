package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.presentation.dto.request.UpdateMainGoalRequest;
import com.koa.koalamailman.mandalart.domain.Status;

import java.util.List;

public record MainGoalDto (
        Long id,
        int position,
        String content,
        Status status,
        List<SubGoalDto> subs
) {
    public static MainGoalDto fromRequest(UpdateMainGoalRequest req) {
        List<SubGoalDto> subDtos = (req.subs() == null) ? List.of()
                : req.subs().stream().map(SubGoalDto::fromRequest).toList();
        return new MainGoalDto(req.goalId(), req.position(), req.content(), req.status(), subDtos);
    }
}
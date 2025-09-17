package com.koa.koalamailman.domain.mandalart.dto;

import com.koa.koalamailman.domain.mandalart.dto.request.UpdateMainGoalRequest;
import java.util.List;

public record MainGoalDto (
        Long id,
        int position,
        String content,
//        Status status,
        List<SubGoalDto> subs
) {
    public static MainGoalDto fromRequest(UpdateMainGoalRequest req) {
        List<SubGoalDto> subDtos = (req.subs() == null) ? List.of()
                : req.subs().stream().map(SubGoalDto::fromRequest).toList();
        return new MainGoalDto(req.goalId(), req.position(), req.content(), subDtos);
    }
}
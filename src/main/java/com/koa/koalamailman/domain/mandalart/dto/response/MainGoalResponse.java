package com.koa.koalamailman.domain.mandalart.dto.response;

import com.koa.koalamailman.domain.mandalart.dto.MainGoalDto;
import com.koa.koalamailman.domain.mandalart.repository.entity.Status;

import java.util.List;

public record MainGoalResponse(
        Long goalId,
        int position,
        String content,
        Status status,
        List<SubGoalResponse> subs
) {
    public static MainGoalResponse from(MainGoalDto dto) {
        List<SubGoalResponse> subResponses = dto.subs().stream()
                .map(SubGoalResponse::from)
                .toList();

        return new MainGoalResponse(dto.id(), dto.position(), dto.content(), dto.status(), subResponses);
    }
}

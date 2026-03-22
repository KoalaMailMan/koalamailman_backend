package com.koa.koalamailman.mandalart.dto.response;

import com.koa.koalamailman.mandalart.dto.CoreGoalDto;
import com.koa.koalamailman.mandalart.repository.entity.Status;

import java.util.List;

public record CoreGoalResponse(
        Long goalId,
        String content,
        Status status,
        List<MainGoalResponse> mains
) {
    public static CoreGoalResponse from(CoreGoalDto dto) {
        List<MainGoalResponse> mainResponses = dto.mains().stream()
                .map(MainGoalResponse::from)
                .toList();

        return new CoreGoalResponse(dto.id(), dto.content(), dto.status(), mainResponses);
    }
}

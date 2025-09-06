package com.koa.koalamailman.domain.mandalart.dto.response;

import com.koa.koalamailman.domain.mandalart.dto.CoreGoalDto;
import java.util.List;

public record CoreGoalResponse(
        Long goalId,
        String content,
        List<MainGoalResponse> mains
) {
    public static CoreGoalResponse from(CoreGoalDto dto) {
        List<MainGoalResponse> mainResponses = dto.mains().stream()
                .map(MainGoalResponse::from)
                .toList();

        return new CoreGoalResponse(dto.id(), dto.content(), mainResponses);
    }
}

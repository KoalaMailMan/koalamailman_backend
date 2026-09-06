package com.koa.koalamailman.mandalart.presentation.dto.response;

import com.koa.koalamailman.mandalart.application.dto.MandalartDto;
public record MandalartResponse(
        Long mandalartId,
        CoreGoalResponse core
) {
    public static MandalartResponse from(MandalartDto mandalartDto) {
        return new MandalartResponse(mandalartDto.mandalartId(), CoreGoalResponse.from(mandalartDto.coreGoalDto()));
    }
}

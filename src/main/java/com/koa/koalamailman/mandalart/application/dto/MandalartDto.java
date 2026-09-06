package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.domain.Mandalart;
public record MandalartDto(
        Long mandalartId,
        CoreGoalDto coreGoalDto
) {
    public static MandalartDto from(Mandalart mandalart, CoreGoalDto coreGoalDto) {
        return new MandalartDto(mandalart.getId(), coreGoalDto);
    }
}

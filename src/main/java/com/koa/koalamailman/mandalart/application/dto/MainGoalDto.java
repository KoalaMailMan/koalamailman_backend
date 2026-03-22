package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.domain.Status;

import java.util.List;

public record MainGoalDto (
        Long id,
        int position,
        String content,
        Status status,
        List<SubGoalDto> subs
) {
}
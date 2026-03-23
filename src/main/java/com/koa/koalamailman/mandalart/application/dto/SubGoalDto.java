package com.koa.koalamailman.mandalart.application.dto;

import com.koa.koalamailman.mandalart.domain.Status;

public record SubGoalDto(
        Long id,
         int position,
         String content,
       Status status
) {
}
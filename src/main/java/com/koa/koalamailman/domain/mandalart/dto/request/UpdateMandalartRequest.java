package com.koa.koalamailman.domain.mandalart.dto.request;

import jakarta.validation.Valid;

public record UpdateMandalartRequest(
        Long mandalartId,
        @Valid
        UpdateCoreGoalRequest core
) {
}

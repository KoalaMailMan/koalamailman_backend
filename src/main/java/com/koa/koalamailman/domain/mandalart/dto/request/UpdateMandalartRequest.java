package com.koa.koalamailman.domain.mandalart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

public record UpdateMandalartRequest(
        @Schema(description = "create 시에는 X")
        Long mandalartId,

        @Valid
        UpdateCoreGoalRequest core
) {
}

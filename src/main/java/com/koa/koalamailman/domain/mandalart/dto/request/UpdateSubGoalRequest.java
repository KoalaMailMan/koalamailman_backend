package com.koa.koalamailman.domain.mandalart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record UpdateSubGoalRequest (
     Long goalId,
     @NotNull
     @Min(1) @Max(8)
     Integer position,
     @Schema(description = "sub goal 내용")
     String content
){
}

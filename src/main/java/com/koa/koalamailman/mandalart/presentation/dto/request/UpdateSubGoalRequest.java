package com.koa.koalamailman.mandalart.presentation.dto.request;

import com.koa.koalamailman.mandalart.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record UpdateSubGoalRequest (
     @Schema(description = "create 시에는 X")
     Long goalId,

     @NotNull
     @Min(1) @Max(8)
     Integer position,

     @Schema(description = "sub goal 내용")
     String content,

     @Schema(description = "sub goal status")
     Status status
){
}

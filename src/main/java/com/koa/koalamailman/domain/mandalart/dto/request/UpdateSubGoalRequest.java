package com.koa.koalamailman.domain.mandalart.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record UpdateSubGoalRequest (
     Long goalId,
     @NotNull
     @Min(1) @Max(8)
     Integer position,
     String content
     //Status status
){
}

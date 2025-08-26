package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public record UpdateSubGoalRequest (
     Long subGoalId,
     @NotNull
     Integer position,
     String content,
     Status status
){
}

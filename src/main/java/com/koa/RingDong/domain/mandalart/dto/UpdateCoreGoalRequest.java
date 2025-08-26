package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.entity.ReminderInterval;
import com.koa.RingDong.domain.mandalart.repository.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public record UpdateCoreGoalRequest(
        @Size(max = 40, message = "최대 입력 길이(40자)를 초과했습니다.")
        @Schema(description = "Core goal 내용")
        String content,
        ReminderInterval reminderInterval,
        List<UpdateMainGoalRequest> mainGoalRequests
) {
}

package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.ReminderInterval;
import com.koa.RingDong.domain.mandalart.repository.Status;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMainBlockRequest {
    private String content;
    private Status status;
    @NotNull
    private ReminderInterval reminderInterval;

    @NotNull
    @Size(min = 8, max = 8, message = "subBlock은 8개여야 합니다.")
    private List<UpdateSubBlockRequest> subBlockRequests;
}

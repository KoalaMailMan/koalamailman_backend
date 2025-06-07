package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.ReminderInterval;
import com.koa.RingDong.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMainBlockRequest {
    private String content;
    private Status status;
    private ReminderInterval reminderInterval;
    private List<UpdateSubBlockRequest> subBlockRequests;
}

package com.koa.RingDong.dto.response;

import com.koa.RingDong.entity.MainBlock;
import com.koa.RingDong.entity.ReminderInterval;
import com.koa.RingDong.entity.Status;
import com.koa.RingDong.entity.SubBlock;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainBlockResponse {
    private Long mainId;
    private Long userId;
    private String content;
    private Status status;
    private ReminderInterval reminderInterval;
    private List<SubBlockResponse> subBlocks;

    public static MainBlockResponse from(MainBlock mainBlock) {
        return MainBlockResponse.builder()
                .mainId(mainBlock.getMainId())
                .userId(mainBlock.getUserId())
                .content(mainBlock.getContent())
                .reminderInterval(mainBlock.getReminderInterval())
                .status(mainBlock.getStatus())
                .subBlocks(
                        mainBlock.getSubBlocks().stream()
                                .map(SubBlockResponse::from)
                                .toList()
                )
                .build();
    }
}

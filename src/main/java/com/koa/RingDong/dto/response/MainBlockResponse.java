package com.koa.RingDong.dto.response;

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
    private List<SubBlockResponse> subBlockResponses;
}

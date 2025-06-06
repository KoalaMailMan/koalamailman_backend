package com.koa.RingDong.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpdateMainBlockRequest {
    private String content;
    private List<UpdateSubBlockRequest> subBlockRequests;
}

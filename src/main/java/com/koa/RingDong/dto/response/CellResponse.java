package com.koa.RingDong.dto.response;

import com.koa.RingDong.entity.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CellResponse {
    private Integer position;
    private String content;
    private Status status;
}

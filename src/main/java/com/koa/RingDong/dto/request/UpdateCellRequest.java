package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import lombok.Getter;

@Getter
public class UpdateCellRequest {
    private Long cellId;
    private Integer position;
    private String content;
    private Status status;
}

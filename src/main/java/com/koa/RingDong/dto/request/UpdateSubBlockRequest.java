package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateSubBlockRequest {
    private Integer position;
    private String content;
    private Status status;
    private List<UpdateCellRequest> cells;
}
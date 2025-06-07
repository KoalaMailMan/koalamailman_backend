package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubBlockRequest {
    private Long subId;
    private Integer position;
    private String content;
    private Status status;
    private List<UpdateCellRequest> cells;
}
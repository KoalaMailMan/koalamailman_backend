package com.koa.RingDong.dto.response;

import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.Status;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SubBlockResponse {
    private Long subId;
    private Integer position;
    private String content;
    private Status status;
    private List<CellResponse> cellResponses;
}

package com.koa.RingDong.dto.response;

import com.koa.RingDong.entity.Cell;
import com.koa.RingDong.entity.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CellResponse {
    private Integer position;
    private String content;
    private Status status;

    public static CellResponse from(Cell cell) {
        return CellResponse.builder()
                .position(cell.getPosition())
                .content(cell.getContent())
                .status(cell.getStatus())
                .build();
    }
}

package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.Cell;
import com.koa.RingDong.domain.mandalart.repository.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CellResponse {
    private Long cellId;
    private Integer position;
    private String content;
    private Status status;

    public static CellResponse from(Cell cell) {
        return CellResponse.builder()
                .cellId(cell.getCellId())
                .position(cell.getPosition())
                .content(cell.getContent())
                .status(cell.getStatus())
                .build();
    }
}

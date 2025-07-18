package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.Status;
import com.koa.RingDong.domain.mandalart.repository.SubBlock;
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
    private List<CellResponse> cells;

    public static SubBlockResponse from(SubBlock subBlock) {
        return SubBlockResponse.builder()
                .subId(subBlock.getSubId())
                .position(subBlock.getPosition())
                .content(subBlock.getContent())
                .status(subBlock.getStatus())
                .cells(
                        subBlock.getCells().stream()
                                .map(CellResponse::from)
                                .toList()
                )
                .build();
    }
}


package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCellRequest {
    private Integer position;
    private String content;
    private Status status;
}

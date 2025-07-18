package com.koa.RingDong.domain.mandalart.dto;

import com.koa.RingDong.domain.mandalart.repository.Status;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCellRequest {
    private Long cellId;
    @NonNull
    private Integer position;
    private String content;
    private Status status;
}

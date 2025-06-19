package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import jakarta.validation.constraints.NotNull;
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

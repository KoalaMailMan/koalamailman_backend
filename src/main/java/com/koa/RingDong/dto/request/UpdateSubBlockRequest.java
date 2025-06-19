package com.koa.RingDong.dto.request;

import com.koa.RingDong.entity.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    private Integer position;
    private String content;
    private Status status;
    @NotNull
    @Size(min = 8, max = 8, message = "cell은 8개여야 합니다.")
    private List<UpdateCellRequest> cells;
}
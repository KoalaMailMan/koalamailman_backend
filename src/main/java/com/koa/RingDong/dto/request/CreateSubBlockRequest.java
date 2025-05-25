package com.koa.RingDong.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateSubBlockRequest {

    @NotNull
    @Min(0)
    @Max(7)
    private Integer position;

    @Size(max = 30)
    private String content;

    @Valid
    @Size(min = 8, max = 8)
    private List<CreateCellRequest> cells;
}

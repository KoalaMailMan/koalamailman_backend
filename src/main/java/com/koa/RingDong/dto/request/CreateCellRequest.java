package com.koa.RingDong.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCellRequest {

    @NotNull(message = "position is not nullable")
    @Min(value = 0)
    @Max(value = 8)
    private Integer position;

    @Size(max = 30)
    private String content;
}

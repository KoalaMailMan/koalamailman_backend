package com.koa.koalamailman.global.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RequestDataWrapper<T> {
    @Valid
    @NotNull
    private T data;
}
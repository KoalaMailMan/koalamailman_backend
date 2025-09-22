package com.koa.koalamailman.global.dto;

import lombok.Getter;

@Getter
public class RequestDataWrapper<T> {
    private T data;
}
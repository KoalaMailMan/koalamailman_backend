package com.koa.RingDong.global.exception.model;

import com.koa.RingDong.global.exception.ErrorCode;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}

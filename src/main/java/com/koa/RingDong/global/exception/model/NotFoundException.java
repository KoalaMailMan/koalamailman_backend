package com.koa.RingDong.global.exception.model;

import com.koa.RingDong.global.exception.ErrorCode;
public class NotFoundException extends BaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

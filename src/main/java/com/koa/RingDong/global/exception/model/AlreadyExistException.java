package com.koa.RingDong.global.exception.model;

import com.koa.RingDong.global.exception.ErrorCode;

public class AlreadyExistException extends BaseException {
    public AlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}

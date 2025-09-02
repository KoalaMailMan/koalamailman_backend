package com.koa.RingDong.global.exception.model;

import com.koa.RingDong.global.exception.ErrorCode;

public class DuplicateGoalPositionException extends BaseException {
    public DuplicateGoalPositionException(ErrorCode errorCode) {
        super(errorCode);
    }
}

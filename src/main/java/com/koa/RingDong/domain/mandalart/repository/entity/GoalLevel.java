package com.koa.RingDong.domain.mandalart.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoalLevel {
    CORE(0, "핵심 목표"),
    MAIN(1, "주요 목표"),
    SUB(2, "세부 실행계획");

    private final Integer depth;
    private final String description;
}

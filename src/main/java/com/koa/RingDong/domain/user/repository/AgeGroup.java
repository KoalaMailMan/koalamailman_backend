package com.koa.RingDong.domain.user.repository;

import java.util.List;
import java.util.stream.Stream;

public enum AgeGroup {
    TEENAGER("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES("60대 이상"),
    NOT_DISCLOSED("응답하지 않음");

    private final String description;

    AgeGroup(String description) {
        this.description = description;
    }

    public static List<String> getNames() {
        return Stream.of(AgeGroup.values())
                .map(AgeGroup::name)
                .toList();
    }
}

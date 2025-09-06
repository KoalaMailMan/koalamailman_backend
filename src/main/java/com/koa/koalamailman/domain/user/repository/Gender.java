package com.koa.koalamailman.domain.user.repository;

import java.util.List;
import java.util.stream.Stream;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타"),
    NOT_DISCLOSED("응답하지 않음");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public static List<String> getNames() {
        return Stream.of(Gender.values())
                .map(Gender::name)
                .toList();
    }
}

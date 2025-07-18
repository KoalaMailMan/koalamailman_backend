package com.koa.RingDong.domain.mandalart.repository;

public enum ReminderInterval {

    ONE_WEEK(7),
    TWO_WEEKS(14),
    ONE_MONTH(30),
    TWO_MONTHS(60),
    THREE_MONTHS(90),
    FOUR_MONTHS(120),
    SIX_MONTHS(180);

    private final int days;

    ReminderInterval(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}

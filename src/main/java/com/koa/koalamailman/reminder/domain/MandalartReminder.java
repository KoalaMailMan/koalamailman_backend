package com.koa.koalamailman.reminder.domain;

import lombok.Getter;

@Getter
public class MandalartReminder {

    private final String username;
    private final String email;
    private final MailMandalartGrid grid;
    private final String tip;

    public MandalartReminder(
            String username,
            String email,
            MailMandalartGrid grid,
            String tip
    ) {
        this.username = username;
        this.email = email;
        this.grid = grid;
        this.tip = tip;
    }
}

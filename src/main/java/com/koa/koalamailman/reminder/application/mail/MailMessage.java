package com.koa.koalamailman.reminder.application.mail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailMessage {
    private final String from;
    private final String to;
    private final String subject;
    private final String text;
    private final String html;
}

package com.koa.koalamailman.domain.reminder.application;

import com.koa.koalamailman.domain.reminder.domain.EmailMessage;

public interface MailClient {
    String getProvider();
    void send(EmailMessage message);
}

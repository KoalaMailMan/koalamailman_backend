package com.koa.koalamailman.domain.reminder.application.mail;

public interface MailSender {
    String getProvider();
    void send(MailMessage message);
}

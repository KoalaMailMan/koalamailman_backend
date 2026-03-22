package com.koa.koalamailman.reminder.application.mail;

public interface MailSender {
    String getProvider();
    void send(MailMessage message);
}

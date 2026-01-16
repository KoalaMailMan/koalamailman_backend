package com.koa.koalamailman.domain.reminder.infrastructure;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ReminderTaskScheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public void schedule(Runnable task, long delayMs) {
        scheduler.schedule(task, delayMs, TimeUnit.MILLISECONDS);
    }
}

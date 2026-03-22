package com.koa.koalamailman.reminder.presentation;

import com.koa.koalamailman.reminder.application.usecase.ScheduleReminderUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemindSchedulerTrigger {

    private final ScheduleReminderUseCase scheduleReminderUseCase;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void runDaily() {
        scheduleReminderUseCase.scheduleUntilToday();
    }

    @PostConstruct
    public void onStartup() {
        scheduleReminderUseCase.scheduleOnStartupIfNeeded();
    }
}

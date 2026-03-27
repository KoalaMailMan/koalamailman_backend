package com.koa.koalamailman.reminder.application.usecase;

import com.koa.koalamailman.mandalart.application.MandalartUseCase;
import com.koa.koalamailman.mandalart.domain.RemindInterval;
import com.koa.koalamailman.reminder.application.provider.ReminderTimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReScheduleReminderUseCase {

    private final ReminderTimeProvider reminderTimeProvider;
    private final MandalartUseCase mandalartUseCase;

    public void rescheduleRandom(Long mandalartId, RemindInterval interval) {
        LocalDateTime nextScheduledTime = reminderTimeProvider.generateRandomTime(interval);
        mandalartUseCase.rescheduleReminder(mandalartId, nextScheduledTime);
    }
}

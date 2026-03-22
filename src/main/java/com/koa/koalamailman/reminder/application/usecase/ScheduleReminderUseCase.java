package com.koa.koalamailman.reminder.application.usecase;

import com.koa.koalamailman.mandalart.repository.entity.MandalartEntity;
import com.koa.koalamailman.reminder.application.mail.RemindMailService;
import com.koa.koalamailman.reminder.infrastructure.ReminderTaskScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleReminderUseCase {

    private final FindDueReminderUseCase findDueReminderUseCase;
    private final RemindMailService reminderRemindMailService;
    private final ReScheduleReminderUseCase reScheduleReminderUseCase;
    private final ReminderTaskScheduler taskScheduler;

    public void scheduleOnStartupIfNeeded() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixAMToday = LocalDate.now().atTime(6, 0);

        if (now.isBefore(sixAMToday)) log.info("[스케줄러] 🕕 애플리케이션 시작: 오전 6시 이전, 메일 예약 스킵");
        else scheduleUntilToday();
    }

    public void scheduleUntilToday() {
        LocalDateTime until = LocalDate.now().atTime(23, 59, 59);
        var mandalarts = findDueReminderUseCase.findBefore(until);
        log.info("[스케줄러] 📧 메일 예약 대상 (오늘/과거): {}건", mandalarts.size());
        mandalarts.forEach(mandalart -> {
            long delay = calculateDelay(mandalart.getReminderOption().getRemindScheduledAt());

            taskScheduler.schedule(
                    () -> execute(mandalart),
                    delay
            );
        });
    }

    private void execute(MandalartEntity mandalart) {
        reminderRemindMailService.send(mandalart);
        reScheduleReminderUseCase.rescheduleRandom(mandalart.getId());
    }

    private long calculateDelay(LocalDateTime scheduledTime) {
        return Math.max(
                Duration.between(LocalDateTime.now(), scheduledTime).toMillis(),
                0
        );
    }
}

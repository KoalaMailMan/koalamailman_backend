package com.koa.RingDong.domain.reminder.service;

import com.koa.RingDong.domain.mandalart.repository.entity.CoreGoal;
import com.koa.RingDong.domain.reminder.provider.ReminderTimeProvider;
import com.koa.RingDong.domain.mandalart.repository.CoreGoalRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderSchedulerService {

    private final CoreGoalRepository coreGoalRepository;
    private final MailService mailService;
    private final ReminderTimeProvider reminderTimeProvider;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // 매일 오전 6시 실행
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void checkAndScheduleTodayMails() {
        log.info("[스케줄러] 🔔오전 6시 스케줄러 실행");
        reschedulePastOrScheduleToday();
    }

    @PostConstruct
    public void scheduleMailsOnStartup() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayAtSix = LocalDate.now().atTime(6, 0);

        if (now.isBefore(todayAtSix)) {
            log.info("[스케줄러] 🕕애플리케이션 시작: 오전 6시 이전은 메일 예약 스케줄링 X");
            return;
        }

        log.info("[스케줄러] 🚀애플리케이션 시작: 메일 예약 스케줄링 시작");
        reschedulePastOrScheduleToday();
    }

    private void reschedulePastOrScheduleToday() {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(23, 59, 59);

        List<CoreGoal> targets = coreGoalRepository.findByNextScheduledTimeBefore(endOfToday.plusSeconds(1));
        log.info("[스케줄러] 📧오늘 또는 지난 메일 예약 대상 수: {}", targets.size());

        for (CoreGoal core : targets) {
            if (core.getNextScheduledTime().isBefore(startOfToday)) {
                log.info("[스케줄러] 🔄과거 시간 발견 - userId: {}, 원래 시간: {}", core.getUserId(), core.getNextScheduledTime());

                core.updateNextScheduledTime(reminderTimeProvider.generateRandomTime(core.getReminderInterval()));

                log.info("[스케줄러] 🆕새로 설정된 시간: {}", core.getNextScheduledTime());
            }

            if (!core.getNextScheduledTime().isBefore(startOfToday) &&
                    !core.getNextScheduledTime().isAfter(endOfToday)) {
                scheduleMailAt(core.getNextScheduledTime(), core.getUserId());
                log.info("[스케줄러] ⏱️메일 예약 시작 - userId: {}, scheduledTime: {}", core.getUserId(), core.getNextScheduledTime());
            }

            coreGoalRepository.save(core);
        }
    }

    private void scheduleMailAt(LocalDateTime scheduledTime, Long userId) {
        long delay = Duration.between(LocalDateTime.now(), scheduledTime).toMillis();
        scheduler.schedule(() -> {
            try {
                mailService.sendMail(userId);
                log.info("[스케줄러] 메일 전송 완료 - userId: {}", userId);
            } catch (Exception e) {
                log.error("[스케줄러] 메일 전송 중 예외 발생 - userId: {}, 이유: {}", userId, e.getMessage());
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
}

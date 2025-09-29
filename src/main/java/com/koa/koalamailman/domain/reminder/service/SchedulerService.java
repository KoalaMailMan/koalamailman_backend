package com.koa.koalamailman.domain.reminder.service;

import com.koa.koalamailman.domain.mandalart.repository.entity.MandalartEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SchedulerService {

    private final MailService mailService;
    private final ReminderService reminderService;

    private static final int SCHEDULER_POOL_SIZE = 3;
    private static final int MAX_RETRY_COUNT = 3;
    private static final long RETRY_DELAY_MS = TimeUnit.HOURS.toMillis(1); // 1시간
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);


    /**
     * 매일 오전 6시에 실행: 오늘과 과거 예약 메일 모두 스케줄링.
     */
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void scheduleTodayAndPastMails() {
        log.info("[스케줄러] 🔔 오전 6시 스케줄러 실행");
        scheduleMailsBefore(LocalDate.now().atTime(23, 59, 59));
    }

    /**
     * 앱 시작 시: 오늘 오전 6시 이후면 예약/발송 스캔
     */
    @PostConstruct
    public void onStartupScheduleReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixAMToday = LocalDate.now().atTime(6, 0);

        if (now.isBefore(sixAMToday)) {
            log.info("[스케줄러] 🕕 애플리케이션 시작: 오전 6시 이전, 메일 예약 스킵");
            return;
        }
        log.info("[스케줄러] 🚀 애플리케이션 시작: 메일 예약 스케줄 시작");
        scheduleMailsBefore(LocalDate.now().atTime(23, 59, 59));
    }

    /**
     * until 시점 이전의 모든 만달아트 예약/발송
     */
    private void scheduleMailsBefore(LocalDateTime until) {
        List<MandalartEntity> mandalarts = reminderService.findMandalartsByScheduleTimeBefore(until.plusSeconds(1));
        log.info("[스케줄러] 📧 메일 예약 대상 (오늘/과거): {}건", mandalarts.size());
        mandalarts.forEach(this::scheduleMailWithRetry);
    }

    /**
     * 개별 메일 발송 예약 및 재시도
     */
    @Transactional
    public void scheduleMailWithRetry(MandalartEntity mandalart) {
        LocalDateTime scheduledTime = mandalart.getReminderOption().getRemindScheduledAt();
        long delayMs = Math.max(Duration.between(LocalDateTime.now(), scheduledTime).toMillis(), 0);

        scheduler.schedule(() -> {
            try {
                boolean send = sendMailWithRetry(mandalart, 0);
                if (send) updateNextReminder(mandalart);
            } catch (Exception e) {
                log.error("[스케줄러] 스케줄 등록 예외 - userId: {}", mandalart.getUserId(), e);
            }
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 메일 발송 및 실패 시 재시도
     */
    private Boolean sendMailWithRetry(MandalartEntity mandalart, int attempt) {
        try {
            mailService.sendRemindMail(mandalart);
            log.info("[스케줄러] 메일 전송 성공 - userId: {}", mandalart.getUserId());
            return true;
        } catch (Exception e) {
            log.error("[스케줄러] 메일 발송 실패 - userId: {}, 시도: {}, 이유: {}",
                    mandalart.getUserId(), attempt + 1, e.getMessage());

            if (attempt < MAX_RETRY_COUNT) {
                scheduler.schedule(() -> sendMailWithRetry(mandalart, attempt + 1),
                        RETRY_DELAY_MS, TimeUnit.MILLISECONDS);
            } else {
                log.warn("[스케줄러] 최대 재시도 초과, 메일 발송 실패 - userId: {}", mandalart.getUserId());
            }
        }
        return false;
    }

    /**
     * 다음 예약시간 갱신
     */
    private void updateNextReminder(MandalartEntity mandalart) {
        try {
            reminderService.rescheduleRandomWithinInterval(
                    mandalart.getReminderOption(), mandalart);
        } catch (Exception e) {
            log.error("[스케줄러] 예약 시간 갱신 실패 - userId: {}, 이유: {}", mandalart.getUserId(), e.getMessage());
        }
    }
}

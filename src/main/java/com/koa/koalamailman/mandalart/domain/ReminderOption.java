package com.koa.koalamailman.mandalart.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReminderOption {

    @Column(nullable = false)
    @Builder.Default
    private Boolean reminderEnabled = false;

    @Enumerated(EnumType.STRING)
    private RemindInterval remindInterval;

    private LocalDateTime remindScheduledAt;

    void update(Boolean enabled, RemindInterval interval, LocalDateTime nextTime) {
        this.reminderEnabled = enabled;
        this.remindInterval = interval;
        this.remindScheduledAt = nextTime;
    }

    void setRemindScheduledAt(LocalDateTime nextTime) {
        this.remindScheduledAt = nextTime;
    }

    static ReminderOption disabled() {
        return ReminderOption.builder()
                .reminderEnabled(false)
                .remindInterval(null)
                .remindScheduledAt(null)
                .build();
    }
}

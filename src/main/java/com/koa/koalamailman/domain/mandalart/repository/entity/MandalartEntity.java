package com.koa.koalamailman.domain.mandalart.repository.entity;

import com.koa.RingDong.domain.reminder.dto.ReminderDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mandalart", indexes = {
        @Index(
                name = "idx_user_id",
                columnList = "user_id"
        )
},
        uniqueConstraints = {
        @UniqueConstraint(name = "uq_mandalart_user",
                columnNames = {"user_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MandalartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Embedded
    private ReminderOption reminderOption;

    public static MandalartEntity create(
            Long userId
    ) {
        return MandalartEntity.builder()
                .userId(userId)
                .reminderOption(ReminderOption.disabled())
                .build();
    }

    public ReminderDto toReminderDto() {
        return new ReminderDto(
                this.id,
                this.reminderOption.getReminderEnabled(),
                this.reminderOption.getRemindInterval(),
                this.reminderOption.getRemindScheduledAt()
        );
    }
}

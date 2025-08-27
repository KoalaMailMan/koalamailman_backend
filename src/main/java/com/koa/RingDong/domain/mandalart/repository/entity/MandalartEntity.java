package com.koa.RingDong.domain.mandalart.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mandalart", indexes = {
        @Index(
                name = "idx_user_id",
                columnList = "userId"
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class MandalartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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
}

package com.koa.koalamailman.mandalart.domain;

import com.koa.koalamailman.global.entity.BaseEntity;
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
public class Mandalart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Embedded
    private ReminderOption reminderOption;

    public static Mandalart create(
            Long userId
    ) {
        return Mandalart.builder()
                .userId(userId)
                .reminderOption(ReminderOption.disabled())
                .build();
    }
}

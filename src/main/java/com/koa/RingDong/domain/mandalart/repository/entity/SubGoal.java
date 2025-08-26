package com.koa.RingDong.domain.mandalart.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_goal", indexes = {
        @Index(
                name = "idx_main_goal_position",
                columnList = "main_goal_id, position",
                unique = true
        )
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subGoalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_goal_id", nullable = false)
    private MainGoal mainGoal;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = true)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public SubGoal(MainGoal mainGoal, Integer position, String content, Status status) {
        this.mainGoal = mainGoal;
        this.position = position;
        this.content = content;
        this.status = status;
    }

    public void updateSubGoal(String content, Status status) {
        this.content = content;
        this.status = status;
    }
}


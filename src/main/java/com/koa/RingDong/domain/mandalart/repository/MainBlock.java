package com.koa.RingDong.domain.mandalart.repository;

import com.koa.RingDong.domain.mandalart.dto.UpdateMainBlockRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "main_block", indexes = {
        @Index(
                name = "idx_user_id",
                columnList = "userId")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private String content; // 정중앙 content

    @Column(nullable = false)
    private ReminderInterval reminderInterval;

    @Column(nullable = true)
    private LocalDateTime nextScheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "mainBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubBlock> subBlocks = new HashSet<>();

    @Builder
    public MainBlock(Long userId, String content, ReminderInterval reminderInterval, LocalDateTime nextScheduledTime) {
        this.userId = userId;
        this.content = content;
        this.reminderInterval = reminderInterval;
        this.status = Status.UNDONE;
        this.nextScheduledTime = nextScheduledTime;
    }

    public void updateMainBlockField(UpdateMainBlockRequest request, LocalDateTime nextScheduledTime) {
        this.content = request.getContent();
        this.status = request.getStatus();
        this.reminderInterval = request.getReminderInterval();
        this.nextScheduledTime = nextScheduledTime;
    }

    public void updateNextScheduledTime(LocalDateTime nextScheduledTime) {
        this.nextScheduledTime = nextScheduledTime;
    }

    @Override
    public String toString() {
        return "MainBlock{" +
                "mainId=" + mainId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", reminderInterval=" + reminderInterval +
                ", nextScheduledTime=" + nextScheduledTime +
                ", subBlocks=" + subBlocks +
                '}';
    }
}

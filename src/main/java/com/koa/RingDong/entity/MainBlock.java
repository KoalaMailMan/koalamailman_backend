package com.koa.RingDong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mandalart", indexes = {
        @Index(
                name = "idx_user_id",
                columnList = "userId")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mandalartId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private String content; // 정중앙 content

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public MainBlock(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.status = Status.UNDONE;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }
}

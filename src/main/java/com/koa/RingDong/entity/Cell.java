package com.koa.RingDong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cell", indexes = {
        @Index(
                name = "idx_cell_position",
                columnList = "block_id, position",
                unique = true
        )
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cellId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id", nullable = false)
    private SubBlock subBlock;

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
    public Cell(SubBlock subBlock, Integer position, String content) {
        this.subBlock = subBlock;
        this.position = position;
        this.content = content;
        this.status = Status.UNDONE;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }
}


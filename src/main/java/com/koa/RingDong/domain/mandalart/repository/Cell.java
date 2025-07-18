package com.koa.RingDong.domain.mandalart.repository;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cell", indexes = {
        @Index(
                name = "idx_sub_position",
                columnList = "sub_id, position",
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
    @JoinColumn(name = "sub_id", nullable = false)
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
    public Cell(SubBlock subBlock, Integer position, String content, Status status) {
        this.subBlock = subBlock;
        this.position = position;
        this.content = content;
        this.status = status;
    }

    public void updateCellField(String content, Status status) {
        this.content = content;
        this.status = status;
    }
}


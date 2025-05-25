package com.koa.RingDong.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "block", indexes = {
        @Index(
                name = "idx_block_position",
                columnList = "mandalart_id, position",
                unique = true
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mandalart_id", nullable = false)
    private MainBlock mainBlock;

    @Column(nullable = false)
    private Integer position; // 9개

    @Column(nullable = true)
    private String content; // 블럭 중앙 content

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Builder
    public SubBlock(MainBlock mainBlock, Integer position, String content) {
        this.mainBlock = mainBlock;
        this.position = position;
        this.content = content;
        this.status = Status.UNDONE;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }
}

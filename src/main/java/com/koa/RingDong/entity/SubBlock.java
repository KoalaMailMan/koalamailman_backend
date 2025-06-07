package com.koa.RingDong.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_block", indexes = {
        @Index(
                name = "idx_main_position",
                columnList = "main_id, position",
                unique = true
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_id", nullable = false)
    private MainBlock mainBlock;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = true)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "subBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cell> cells = new HashSet<>();

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
        if (newStatus != null) this.status = newStatus;
    }
}

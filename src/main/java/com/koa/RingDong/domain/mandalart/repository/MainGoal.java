package com.koa.RingDong.domain.mandalart.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "main_goal", indexes = {
        @Index(
                name = "idx_core_position",
                columnList = "core_goal_id, position",
                unique = true
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mainGoalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "core_goal_id", nullable = false)
    private CoreGoal coreGoal;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = true)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToMany(mappedBy = "mainGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubGoal> subGoals = new HashSet<>();

    @Builder
    public MainGoal(CoreGoal coreGoal, Integer position, String content, Status status) {
        this.coreGoal = coreGoal;
        this.position = position;
        this.content = content;
        this.status = status;
    }

    public void updateMainGoal(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    @Override
    public String toString() {
        return "MainGoal{" +
                "mainGoalId=" + mainGoalId +
                ", position=" + position +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", subGoals=" + subGoals.size() + "개" +
                '}';
    }
}

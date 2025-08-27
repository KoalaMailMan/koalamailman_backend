package com.koa.RingDong.domain.mandalart.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "goal", indexes = {
        @Index(
                name = "idx_mandalart_id",
                columnList = "mandalartId"
        )
})
public class GoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(nullable = false)
    private Long mandalartId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalLevel level;

    private Long parentId;

    @Column(nullable = false)
    private Integer position;

    @Column(length = 40)
    private String content;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Status status;


    public static GoalEntity createCoreGoal(
            Long mandalartId,
            String content
    ) {
        return GoalEntity.builder()
                .mandalartId(mandalartId)
                .level(GoalLevel.CORE)
                .parentId(null)
                .position(0)
                .content(content)
                .build();
    }

    public static GoalEntity createMainGoal(
            Long mandalartId,
            Long coreId,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalartId(mandalartId)
                .level(GoalLevel.MAIN)
                .parentId(coreId)
                .position(position)
                .content(content)
                .build();
    }

    public static GoalEntity createSubGoal(
            Long mandalartId,
            Long mainId,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalartId(mandalartId)
                .level(GoalLevel.SUB)
                .parentId(mainId)
                .position(position)
                .content(content)
                .build();
    }

    public void updateGoalInfo(String content) {
        this.content = content;
    }
}

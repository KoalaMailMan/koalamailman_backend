package com.koa.RingDong.domain.mandalart.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "goal",
        indexes = {
            @Index(
                    name = "idx_mandalart_id",
                    columnList = "mandalartId"
            )
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_mandalart",
                        columnNames = {"mandalartId", "level", "parentPosition", "position"})
        }

)
public class GoalEntity {

    public static final int CORE_POSITION = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(nullable = false)
    private Long mandalartId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalLevel level;

    @Column(nullable = false)
    private Integer position; // CORE: 0, MAIN: 1~8, SUB: 1~8

    @Column
    private Integer parentPosition; // CORE: null, MAIN: 0, SUB: 1~8

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
                .parentPosition(null)
                .position(CORE_POSITION)
                .content(content)
                .build();
    }

    public static GoalEntity createMainGoal(
            Long mandalartId,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalartId(mandalartId)
                .level(GoalLevel.MAIN)
                .parentPosition(CORE_POSITION)
                .position(position)
                .content(content)
                .build();
    }

    public static GoalEntity createSubGoal(
            Long mandalartId,
            Integer mainPosition,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalartId(mandalartId)
                .level(GoalLevel.SUB)
                .parentPosition(mainPosition)
                .position(position)
                .content(content)
                .build();
    }

    public void updateGoalInfo(String content) {
        this.content = content;
    }
}

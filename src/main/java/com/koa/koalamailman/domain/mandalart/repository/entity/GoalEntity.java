package com.koa.koalamailman.domain.mandalart.repository.entity;

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
                    columnList = "mandalart_id"
            )
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_mandalart",
                        columnNames = {"mandalart_id", "level", "parent_position", "position"})
        }

)
public class GoalEntity {

    public static final int CORE_POSITION = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mandalart_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_goal_mandalart"))
    private MandalartEntity mandalart;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private GoalLevel level;

    @Column(name = "position", nullable = false)
    private Integer position; // CORE: 0, MAIN: 1~8, SUB: 1~8

    @Column(name = "parent_position")
    private Integer parentPosition; // CORE: null, MAIN: 0, SUB: 1~8

    @Column(name = "content", length = 40)
    private String content;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Status status;


    public static GoalEntity createCoreGoal(
            MandalartEntity mandalart,
            String content
    ) {
        return GoalEntity.builder()
                .mandalart(mandalart)
                .level(GoalLevel.CORE)
                .parentPosition(null)
                .position(CORE_POSITION)
                .content(content)
                .build();
    }

    public static GoalEntity createMainGoal(
            MandalartEntity mandalart,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalart(mandalart)
                .level(GoalLevel.MAIN)
                .parentPosition(CORE_POSITION)
                .position(position)
                .content(content)
                .build();
    }

    public static GoalEntity createSubGoal(
            MandalartEntity mandalart,
            Integer mainPosition,
            Integer position,
            String content
    ) {
        return GoalEntity.builder()
                .mandalart(mandalart)
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

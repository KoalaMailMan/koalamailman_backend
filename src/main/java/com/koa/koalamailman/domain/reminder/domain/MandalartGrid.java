package com.koa.koalamailman.domain.reminder.domain;

import com.koa.koalamailman.domain.mandalart.repository.entity.GoalEntity;
import com.koa.koalamailman.domain.mandalart.repository.entity.GoalLevel;

import java.util.List;

public class MandalartGrid {

    private final String[][] values;

    private MandalartGrid(String[][] values) {
        this.values = values;
    }

    public static MandalartGrid from(List<GoalEntity> goals) {
        int size = 3;
        String[][] grid = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = "";
            }
        }

        for (GoalEntity goal : goals) {
            if (goal.getLevel() == GoalLevel.CORE) {
                grid[1][1] = goal.getContent();
            } else if (goal.getLevel() == GoalLevel.MAIN) {
                int pos = goal.getPosition();
                int row = pos / size;
                int col = pos % size;

                if (row == 1 && col == 1) continue;
                grid[row][col] = goal.getContent();
            }
        }

        return new MandalartGrid(grid);
    }

    public String[][] values() {
        return values;
    }
}

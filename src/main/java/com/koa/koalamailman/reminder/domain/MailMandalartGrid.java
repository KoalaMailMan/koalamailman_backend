package com.koa.koalamailman.reminder.domain;

import com.koa.koalamailman.mandalart.domain.Goal;
import com.koa.koalamailman.mandalart.domain.GoalLevel;

import java.util.List;

public class MailMandalartGrid {

    private final String[][] values;

    private MailMandalartGrid(String[][] values) {
        this.values = values;
    }

    public static MailMandalartGrid from(List<Goal> goals) {
        int size = 3;
        String[][] grid = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = "";
            }
        }

        for (Goal goal : goals) {
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

        return new MailMandalartGrid(grid);
    }

    public String[][] values() {
        return values;
    }
}

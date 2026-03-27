package com.koa.koalamailman.recommend.application.port;


import reactor.core.publisher.Flux;

import java.util.List;

public interface GoalRecommender {
    String call(String parentGoal, int count, List<String> excludeGoals);
    Flux<String> stream(String parentGoal, int count, List<String> excludeGoals);
}


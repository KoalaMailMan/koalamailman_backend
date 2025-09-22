package com.koa.koalamailman.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class SchedulerLoggingAspect {

    @Pointcut("execution(* com.koa.koalamailman.domain.reminder..*(..))")
    public void schedulerMethods() {}

    @Before("schedulerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        log.info("[SCHEDULER START] {}.{}() traceId={}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                traceId);
    }

    @AfterReturning(pointcut = "schedulerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[SCHEDULER END] {}.{}() completed",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        MDC.clear();
    }

    @AfterThrowing(pointcut = "schedulerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("[SCHEDULER ERROR] Exception in {}.{}() - {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage(), ex);
        MDC.clear();
    }
}

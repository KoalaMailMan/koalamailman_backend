package com.koa.koalamailman.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("execution(* com.koa.koalamailman.domain..controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("[REQUEST] {} {} traceId={}", method, uri, traceId);
        log.debug("→ {}.{}() called", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        int status = request.getAttribute("javax.servlet.error.status_code") != null
                ? (int) request.getAttribute("javax.servlet.error.status_code")
                : 200;

        log.info("[RESPONSE] {} OK from {}.{}()",
                status,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());

        MDC.clear();
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) throws Throwable {
        log.error("[ERROR] Exception in {}.{}() - {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());

        log.debug("Stacktrace: ", ex);
        MDC.clear();
        throw ex;
    }
}

package com.tommykim.aspectj.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@Aspect
public class AspectV4 {

    @Pointcut("execution(* *..*ControllerV4.*(..))")
    public void Controller() { }

    @Pointcut("execution(* *..*ServiceV4.*(..))")
    public void Service(){ }

    @Pointcut("execution(* *..*RepositoryV4.*(..))")
    public void Repository(){}

    @Around("Controller()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable{
        String sectionId = UUID.randomUUID().toString().substring(0, 8);
        try {
            MDC.put("sectionId", sectionId);
            log.info("[sectionId - {}] {} {} 실행", sectionId, joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            log.info("[sectionId - {}] {}", sectionId, e.getMessage());
            throw e;
        } finally {
            log.info("문제 해결");
            MDC.clear();
        }

    }

    @Around("Service()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String sectionId = MDC.get("sectionId");
        try {
            log.info("[sectionId - {}] {} {} 실행", sectionId, joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            log.info("[sectionId - {}] {}", sectionId,e.getMessage());
            throw e;
        }
    }

    @Around("Repository()")
    public Object repositoryLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String sectionId = MDC.get("sectionId");
        try {
            log.info("[sectionId - {}] {} {} 실행", sectionId, joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            log.info("[sectionId - {}] {}", sectionId, e.getMessage());
            throw e;
        }

    }
}

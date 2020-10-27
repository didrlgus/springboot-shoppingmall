package com.shoppingmall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 AOP는 핵심 비즈니스 기능과 공통 기능으로 '구분'하고, 공통 기능을 개발자의 코드 밖에서 필요한 시점에 적용하는 프로그래밍 방법
 AOP는 대표적으로 로깅, 트랜잭션, 보안처리를 할 때 사용
 여기서는 로깅처리 수행
 **/

@Slf4j
@Aspect
@Component
public class LoggingAspect {
//
//    // 모든 핵심업무 호출 전에 수행
//    @Before("execution(* com.shoppingmall.service..*Service.*(..))")
//    public void initLogging(JoinPoint jp) {
//
//        // 핵심업무(서비스)의 클래스, 메서드, 매개변수 로깅처리
//        log.info("### 핵심업무 코드정보 : " + jp.getSignature());
//        log.info("### 메서드 : " + jp.getSignature().getName());
//        log.info("### 매개변수 : " + Arrays.toString(jp.getArgs()));
//    }
//
//    // 모든 Rest api 요청 시 실행 됨
//    @Around("execution(* com.shoppingmall..*RestController.*(..))")
//    public Object timeLogging(ProceedingJoinPoint pjp) throws Throwable {
//        // 핵심업무 실행 전 시간
//        long start = System.currentTimeMillis();
//        // 핵심업무 실행
//        Object result = pjp.proceed();
//        // 핵심업무 실행 후 시간
//        long end = System.currentTimeMillis();
//
//        log.info("### " + pjp.getSignature().getName() + " 메서드 실행시간 : {}", (end - start) + "ms");
//
//        return result;
//    }
}

package com.spring.aop.springaop.webLogAop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 这个aop是所有的getMapping都会打印对应的日志
 */

@Aspect
@Component
@Order(2)     //增加优先级
@Slf4j
public class LogAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void logAdvicePointCut() {}

    @Before("logAdvicePointCut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info(joinPoint.getSignature().getName());
    }
}

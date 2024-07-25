package com.spring.aop.springaop.webLogAop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect  //首先定义为切面类
@Component   //spring能扫描自动装配
@Slf4j
public class WebLogAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Pointcut("execution(* com.spring.aop.springaop..*.*(..))")
    public void webLog() {}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        //记录接口请求的开始时间
        startTime.set(System.currentTimeMillis());

        //获取当前request请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //解析当前请求
        log.info("IP：{}", request.getRemoteAddr());
        log.info("METHOD:{}", request.getMethod());
        log.info("URI:{}", request.getRequestURI());
        log.info("URL:{}", request.getRequestURL());
        log.info("ARGS:{}", joinPoint.getArgs());
    }

    @AfterReturning(returning = "returnValue", pointcut = "webLog()")
    public void doAfterReturning(Object returnValue) {
        log.info("RESPONSE:{}", returnValue);
        log.info("TIME:{}", System.currentTimeMillis() - startTime.get());
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Arround 之前执行");
        String proceed = (String)joinPoint.proceed();
        proceed += "增强返回值";
        log.info("Arround 之后执行"+proceed);
        return proceed;
    }
}

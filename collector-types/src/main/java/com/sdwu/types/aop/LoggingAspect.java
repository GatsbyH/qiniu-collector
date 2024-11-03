package com.sdwu.types.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(com.sdwu.types.annotation.Loggable)")
    public void loggableMethods(){

    }

    @Around("loggableMethods()")
    public Object logMethod(ProceedingJoinPoint joinPoint) throws  Throwable{
        //记录方法开始时间
        long startTime = System.currentTimeMillis();

        //执行原始方法
        Object result = joinPoint.proceed();

        //记录方法结束时间
        long endTime = System.currentTimeMillis();


        // 计算方法执行时间（转换为秒）
        double executionTime = (endTime - startTime) / 1000.0;
        //记录日志
        logger.info("方法{}执行时间为{}秒",joinPoint.getSignature().toShortString(),executionTime);
        return result;
    }
}

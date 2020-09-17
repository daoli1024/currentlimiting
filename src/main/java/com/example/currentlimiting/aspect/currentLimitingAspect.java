package com.example.currentlimiting.aspect;

import com.example.currentlimiting.rateLimiterService.rateLimiterInterface;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class currentLimitingAspect {
    private Logger logger = LoggerFactory.getLogger(currentLimitingAspect.class);

    @Autowired
    //@Qualifier("rateLimiterService")//仅适用于单个服务器
    @Qualifier("redisRateLimiterService")//分布式服务器和单个服务器都适用
    rateLimiterInterface rateLimiter;

    @Pointcut("execution(public * com.example.currentlimiting.controller.testController.*(..))")
    public void currentLimiting(){}

    @Around("currentLimiting()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        boolean hasToken = rateLimiter.tryAquare();

        Object result = null;
        if(hasToken){
            logger.info("hasToken\n");
            result = pjp.proceed();
        }
        else {
            logger.info("don't hasToken\n");
        }

        return result;
    }

}

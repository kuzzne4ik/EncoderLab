package com.api.component;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.api.service.*.*(..))")
    public void logBeforeMethodExecution() {
        LOGGER.info("Method is about to execute");
    }

    @AfterReturning(pointcut = "execution(* com.api.service.*.*(..))", returning = "result")
    public void logAfterMethodExecution(Object result) {
        LOGGER.info("Method execution completed with result: {}", result);
    }

    @AfterThrowing(pointcut = "execution(* com.api.service.*.*(..))", throwing = "exception")
    public void logAfterMethodThrowing(Exception exception) {
        LOGGER.error("Exception occurred during method execution: {}", exception.getMessage());
    }
}

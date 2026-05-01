package com.acme.order.bootstrap.spring.rest.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect
{

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /*
    // Pointcut that matches all methods in your service package
    @Pointcut("within(com.acme.order..*)")
    public void applicationPackagePointcut()
    {
    }

    // Advice that logs the method entry, execution time, and exit
    @Around("applicationPackagePointcut()")
    */

    // Target any public method within your Controller classes
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    @Around("restControllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        // Retrieve traceId from MDC
        String traceId = MDC.get("traceId");

        // Trace entry
        log.trace("ENTER [TraceId: {}]: {}() with arguments = {}", traceId, methodName, joinPoint.getArgs());

        long startTime = System.currentTimeMillis();

        try
        {
            // Execute the actual method
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;

            // Trace exit (success)
            log.trace("EXIT [TraceId: {}]: {}() successfully (Time taken: {} ms)", traceId, methodName, timeTaken);

            return result;
        }
        catch (IllegalArgumentException e)
        {
            log.error("Illegal argument: [TraceId: {}] in {}() with message = {}", traceId, methodName, e.getMessage());
            throw e;
        }
        catch (Exception e)
        {
            log.error("Exception [TraceId: {}] in {}() with message = {}", traceId, methodName, e.getMessage());
            throw e;
        }
    }

    @Bean
    public CommandLineRunner checkAspect(ApplicationContext ctx)
    {
        return args -> {
            System.out.println("Is LoggingAspect loaded? " + ctx.containsBean("loggingAspect"));
        };
    }
}
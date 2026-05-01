package com.acme.order.bootstrap.spring.rest.config;

import java.util.Map;
import java.util.concurrent.Executor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/*
 * OutboxRelay uses @Async. By default, MDC is thread-local and will not move to the background thread. We fix this in the Bootstrap layer with a TaskDecorator.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        TaskDecorator contextDecorator = new ContextPropagatingTaskDecorator();
        executor.setTaskDecorator(runnable -> {
            Runnable contextAwareRunnable = contextDecorator.decorate(runnable);
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    if (contextMap != null) MDC.setContextMap(contextMap);
                    contextAwareRunnable.run();
                } finally {
                    MDC.clear();
                }
            };
        });
        executor.initialize();
        return executor;
    }
}

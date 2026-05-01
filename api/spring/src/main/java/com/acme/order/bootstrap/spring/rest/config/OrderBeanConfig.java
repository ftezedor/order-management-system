package com.acme.order.bootstrap.spring.rest.config;

import com.acme.order.application.ports.out.LogContextPort;
import com.acme.order.application.service.CreateOrderService;
import com.acme.order.application.service.ListOrdersService;
import com.acme.order.domain.ports.in.CreateOrderUseCase;
import com.acme.order.domain.ports.out.EventPublisher;
import com.acme.order.domain.ports.out.OrderMetricsPort;
import com.acme.order.domain.ports.out.OrderRepository;
import com.acme.order.infrastructure.logging.LogContext;
import com.acme.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfig {

    @Bean
    public OrderPersistenceMapper orderPersistenceMapper() {
        return new OrderPersistenceMapper();
    }

    @Bean
    public ListOrdersService getOrdersService(OrderRepository repository, LogContextPort logContext) {
        return new ListOrdersService(repository, logContext);
    }

    @Bean
    public EventPublisher eventPublisher(ApplicationEventPublisher springPublisher) {
        // We bridge Spring's publisher to our Zero-Spring Infrastructure Adapter
        // Using a lambda to satisfy the 'publish' call
        return springPublisher::publishEvent;
    }

    @Bean
    public LogContextPort logContext() {
        return new LogContext();
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(LogContextPort logContext, OrderRepository repository,
            EventPublisher publisher, OrderMetricsPort metrics) {
        // Injecting the OrderJpaAdapter (which is a @Component) into the pure Service
        return new CreateOrderService(logContext, repository, publisher, metrics);
    }
}
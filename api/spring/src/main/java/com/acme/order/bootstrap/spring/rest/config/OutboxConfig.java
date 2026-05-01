package com.acme.order.bootstrap.spring.rest.config;

import com.acme.order.infrastructure.messaging.RabbitMqOrderPublisher;
import com.acme.order.infrastructure.persistence.adapter.JpaOutboxAdapter;
import com.acme.order.infrastructure.persistence.outbox.OutboxProcessor;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfig {

    @Bean
    public JpaOutboxAdapter jpaOutboxAdapter(EntityManager entityManager) {
        // Now Spring knows how to inject this into your OrderEventListener
        return new JpaOutboxAdapter(entityManager);
    }

    @Bean
    public OutboxProcessor outboxProcessor(EntityManager entityManager, RabbitMqOrderPublisher publisher) {
        return new OutboxProcessor(entityManager, publisher);
    }
    
}
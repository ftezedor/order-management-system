package com.acme.order.bootstrap.spring.rest.config;

import com.acme.order.domain.ports.out.OrderRepository;
import com.acme.order.infrastructure.persistence.adapter.OrderJpaAdapter;
import com.acme.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class PersistenceConfig {

    @Bean
    public OrderRepository orderRepository(EntityManager entityManager) {
        return new OrderJpaAdapter(entityManager, new OrderPersistenceMapper());
    }
}
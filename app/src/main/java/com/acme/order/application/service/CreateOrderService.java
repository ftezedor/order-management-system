package com.acme.order.application.service;

import com.acme.order.application.ports.out.LogContextPort;
import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.domain.model.Order;
import com.acme.order.domain.ports.in.CreateOrderCommand;
import com.acme.order.domain.ports.in.CreateOrderUseCase;
import com.acme.order.domain.ports.out.EventPublisher;
import com.acme.order.domain.ports.out.OrderMetricsPort;
import com.acme.order.domain.ports.out.OrderRepository;
import java.time.Instant;
import java.util.UUID;

public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepository repository;
    private final EventPublisher publisher;
    private final OrderMetricsPort metrics;
    private final LogContextPort logContext;

    public CreateOrderService(LogContextPort logContext, OrderRepository repository, EventPublisher eventPublisher,
            OrderMetricsPort metrics) {
        this.logContext = logContext;
        this.repository = repository;
        this.publisher = eventPublisher;
        this.metrics = metrics;
    }

    @Override
    public Order execute(CreateOrderCommand command) {

        try {

            // 1. Create the Domain Entity (Logic happens here)
            Order order = new Order(
                    UUID.randomUUID(),
                    command.customerId(),
                    command.items());

            Order newOrder = repository.save(order);

            logContext.put("orderId", newOrder.getId().toString());

            // Fire and forget (from the perspective of the business logic)
            publisher.publish(new OrderCreatedEvent(
                    newOrder.getId(),
                    command.customerEmail(),
                    Instant.now()));

            // 2. Record some metrics
            metrics.countOrderCreated();

            return newOrder;

        } finally {

            logContext.clear();

        }
    }
}

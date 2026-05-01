package com.acme.order.domain.ports.out;

import com.acme.order.domain.event.OrderCreatedEvent;

public interface EventPublisher {
    void publish(OrderCreatedEvent event);
}
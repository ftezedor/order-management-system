package com.acme.order.infrastructure.event;

import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.domain.ports.out.EventPublisher;
import jakarta.enterprise.event.Event;

public class EventPublisherAdapter implements EventPublisher {

    private final Event<Object> eventSource;

    public EventPublisherAdapter(Event<Object> eventSource) {
        this.eventSource = eventSource;
    }

    @Override
    public void publish(OrderCreatedEvent event) {
        eventSource.fire(event);
    }
}
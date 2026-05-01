package com.acme.order.bootstrap.spring.rest.listener;

import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.infrastructure.persistence.adapter.JpaOutboxAdapter;
import com.acme.order.infrastructure.persistence.outbox.OutboxEntity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component // This makes it a standalone bean managed by Spring
public class OrderEventListener {

    private final JpaOutboxAdapter outboxAdapter;

    public OrderEventListener(JpaOutboxAdapter outboxAdapter) {
        this.outboxAdapter = outboxAdapter;
    }

    @EventListener
    @Transactional
    public void onOrderCreated(OrderCreatedEvent event) {

        final String payload = String.format(
            """
            {
                "orderId": "%s",
                "customerEmail": "%s",
                "occurredAt": "%s"
            }
            """, 
            event.orderId(), event.customerEmail(), event.occurredAt()
        );

        final OutboxEntity outbox = new OutboxEntity();
        outbox.setAggregateType("ORDER");
        outbox.setEventType("OrderCreated");
        outbox.setPayload(payload);
        outboxAdapter.save(outbox);

    }
}
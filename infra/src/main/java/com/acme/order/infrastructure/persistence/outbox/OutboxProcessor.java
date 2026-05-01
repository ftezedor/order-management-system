package com.acme.order.infrastructure.persistence.outbox;

import com.acme.order.infrastructure.messaging.RabbitMqOrderPublisher;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OutboxProcessor {
    private static final Logger log = LoggerFactory.getLogger(OutboxProcessor.class);

    private final EntityManager entityManager;
    private final RabbitMqOrderPublisher rabbitPublisher;

    public OutboxProcessor(EntityManager entityManager, RabbitMqOrderPublisher rabbitPublisher) {
        this.entityManager = entityManager;
        this.rabbitPublisher = rabbitPublisher;
    }

    public void process() {
        // Query for unprocessed events
        List<OutboxEntity> events = entityManager.createQuery(
            "SELECT o FROM OutboxEntity o WHERE o.processed = false", OutboxEntity.class)
            .setMaxResults(10)
            .getResultList();

        for (OutboxEntity outbox : events) {
            try {

                // Ensure your publisher can handle the payload string
                rabbitPublisher.handle(outbox);
                
                outbox.setProcessed(true);
                // In pure JPA, we ensure the state is managed
                entityManager.merge(outbox);

            } catch (Exception e) {

                log.error("Failed to publish outbox event {}", outbox.getId(), e);
                break;
                
            }
        }
    }
}

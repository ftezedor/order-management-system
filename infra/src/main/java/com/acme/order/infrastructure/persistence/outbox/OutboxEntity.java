package com.acme.order.infrastructure.persistence.outbox;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEntity {
    @Id
    private UUID id = UUID.randomUUID();

    private String aggregateType; // e.g., "ORDER"
    private String eventType;     // e.g., "OrderCreated"
    
    @Column(columnDefinition = "TEXT")
    private String payload;       // The JSON message

    private Instant createdAt = Instant.now();
    private boolean processed = false;

    // Getters and Setters
    public UUID getId() { return id; }
    public String getAggregateType() { return aggregateType; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isProcessed() { return processed; }

    public void setProcessed(boolean processed) { this.processed = processed; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setId(UUID id) { this.id = id; }
}
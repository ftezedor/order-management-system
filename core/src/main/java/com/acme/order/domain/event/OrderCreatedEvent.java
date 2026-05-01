package com.acme.order.domain.event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId,
    String customerEmail,
    Instant occurredAt
) {}
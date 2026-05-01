package com.acme.order.infrastructure.persistence.adapter;

import com.acme.order.infrastructure.persistence.outbox.OutboxEntity;
import jakarta.persistence.EntityManager;


public class JpaOutboxAdapter {
    private final EntityManager entityManager;

    public JpaOutboxAdapter(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(OutboxEntity entity) {
        entityManager.persist(entity);
    }
}
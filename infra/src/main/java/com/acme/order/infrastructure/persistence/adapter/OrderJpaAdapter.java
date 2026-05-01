package com.acme.order.infrastructure.persistence.adapter;

import com.acme.order.domain.model.Order;
import com.acme.order.domain.ports.out.OrderRepository;
import com.acme.order.infrastructure.persistence.jpa.OrderJpaEntity;
import com.acme.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// NOTICE: No @Repository or @Component annotation here!
public class OrderJpaAdapter implements OrderRepository {

    private static final Logger log = LoggerFactory.getLogger(OrderJpaAdapter.class);

    private final EntityManager entityManager;
    private final OrderPersistenceMapper mapper;

    public OrderJpaAdapter(EntityManager entityManager, OrderPersistenceMapper mapper) {
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = mapper.toJpaEntity(order);
        
        if (entityManager.find(OrderJpaEntity.class, jpaEntity.getId()) == null) {
            entityManager.persist(jpaEntity);
        } else {
            jpaEntity = entityManager.merge(jpaEntity);
        }
        
        return mapper.toDomain(jpaEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(OrderJpaEntity.class, id))
                       .map(mapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        try {
            log.info("OrderJpaAdapter.findAll() called");
            return entityManager.createQuery("SELECT o FROM OrderJpaEntity o", OrderJpaEntity.class)
                           .getResultList()
                           .stream()
                           .map(mapper::toDomain)
                           .toList();
        } finally {
            log.info("OrderJpaAdapter.findAll() finished");
        }
    }

    
}
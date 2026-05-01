package com.acme.order.bootstrap.spring.rest.hints;

import com.acme.order.bootstrap.spring.rest.OrderItemResponse;
import com.acme.order.bootstrap.spring.rest.OrderResponse;
import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.domain.model.Order;
import com.acme.order.domain.model.OrderItem;
import com.acme.order.domain.model.OrderStatus;
import com.acme.order.infrastructure.persistence.outbox.OutboxEntity;
import java.util.List;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;


public class OrderRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // 1. Domain Models (Required for Internal Mapping & Logging)
        registerForReflection(hints, Order.class);
        registerForReflection(hints, OrderItem.class);
        registerForReflection(hints, OrderStatus.class);

        // 2. DTOs (Required for Jackson JSON Serialization)
        registerForReflection(hints, OrderResponse.class);
        registerForReflection(hints, OrderItemResponse.class);

        // 3. Events & Infrastructure (Required for Messaging & Persistence)
        registerForReflection(hints, OrderCreatedEvent.class);
        registerForReflection(hints, OutboxEntity.class);
        
        // 4. Register common collection types used in your DTOs
        hints.reflection().registerType(List.class, MemberCategory.INVOKE_PUBLIC_METHODS);
    }

    private void registerForReflection(RuntimeHints hints, Class<?> clazz) {
        hints.reflection().registerType(clazz, 
            MemberCategory.INVOKE_PUBLIC_METHODS, 
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.DECLARED_FIELDS);
    }
}
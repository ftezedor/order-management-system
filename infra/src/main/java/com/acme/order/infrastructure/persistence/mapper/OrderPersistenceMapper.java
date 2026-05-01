package com.acme.order.infrastructure.persistence.mapper;

import com.acme.order.domain.model.Order;
import com.acme.order.domain.model.OrderItem;
import com.acme.order.infrastructure.persistence.jpa.*;

public class OrderPersistenceMapper {

    public OrderPersistenceMapper() {
        // Private constructor to prevent instantiation
    }

    public OrderJpaEntity toJpaEntity(Order order) {
        return OrderJpaEntity.builder()
            .id(order.getId())
            .customerId(order.getCustomerId())
            .status(order.getStatus())
            .items(order.getItems().stream().map(this::toJpaItemEntity).toList())
            .build();
    }

    public Order toDomain(OrderJpaEntity jpaEntity) {
        return Order.builder()
            .id(jpaEntity.getId())
            .customerId(jpaEntity.getCustomerId())
            .items(jpaEntity.getItems().stream().map(this::toOrderItem).toList())
            .status(jpaEntity.getStatus())
            .build();
    }

    private OrderItem toOrderItem(OrderItemJpaEntity item) {
        return OrderItem.builder()
            .productId(item.getProductId())
            .quantity(item.getQuantity())
            .price(item.getPrice())
            .build();
    }

    private OrderItemJpaEntity toJpaItemEntity(OrderItem item) {
        return OrderItemJpaEntity.builder()
            .productId(item.getProductId())
            .quantity(item.getQuantity())
            .price(item.getPrice()) 
            .build();
    }
}
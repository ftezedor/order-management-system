package com.acme.order.bootstrap.spring.rest;

import com.acme.order.domain.model.OrderItem;
import java.math.BigDecimal;
import java.util.UUID;


public record OrderItemResponse(
    UUID id,
    UUID productId,
    Integer quantity,
    BigDecimal price
) {
    /**
     * Maps a Domain OrderItem to a Response DTO.
     * Note: We do NOT include the OrderId here to avoid 
     * infinite recursion during JSON serialization.
     */
    public static OrderItemResponse fromDomain(OrderItem item) {
        return new OrderItemResponse(
            //item.getId(),
            new UUID(0, 0),
            UUID.fromString(item.getProductId()),
            item.getQuantity(),
            item.getPrice()
        );
    }
}
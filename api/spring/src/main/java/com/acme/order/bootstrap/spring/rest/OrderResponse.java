
package com.acme.order.bootstrap.spring.rest;

import com.acme.order.domain.model.Order;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    String customerId,
    String status,
    List<OrderItemResponse> items // This DTO should NOT link back to OrderResponse
) {
    public static OrderResponse fromDomain(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getCustomerId(),
            order.getStatus().name(),
            order.getItems().stream()
                 .map(OrderItemResponse::fromDomain)
                 .toList()
        );
    }
}
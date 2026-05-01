package com.acme.order.domain.model;

import java.util.List;
import java.util.UUID;

public class Order {
    protected final UUID id;
    protected final String customerId;
    protected final List<OrderItem> items;
    protected OrderStatus status;

    // The domain enforces its own consistency
    public Order(UUID id, String customerId, List<OrderItem> items) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        this.id = id;
        this.customerId = customerId;
        this.items = List.copyOf(items); // Immutability 
        this.status = OrderStatus.CREATED;
    }

    public void markAsPaid() {
        if (this.status != OrderStatus.CREATED) {
            throw new IllegalStateException("Order cannot be paid in status: " + status);
        }
        this.status = OrderStatus.PAID;
    }

    // Getters only - no setters to maintain encapsulation
    public UUID getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String customerId;
        private List<OrderItem> items;
        private OrderStatus status;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Order build() {
            Order order = new Order(id, customerId, items);
            order.status = status != null ? status : OrderStatus.CREATED; // Default to CREATED if not set
            return order;
        }
    }
}
package com.acme.order.domain.model;

import java.math.BigDecimal;

public record OrderItem(String productId, int quantity, BigDecimal price) {
    public OrderItem {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String productId;
        private int quantity;
        private BigDecimal price;

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(productId, quantity, price);
        }
    }
}
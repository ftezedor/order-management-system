package com.acme.order.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id; // Internal DB primary key

    @Column(nullable = false)
    protected String productId;

    @Column(nullable = false)
    protected int quantity;

    @Column(nullable = false)
    protected BigDecimal price;

    // Default constructor for JPA
    protected OrderItemJpaEntity() {}

    public OrderItemJpaEntity(String productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public Long getId() { return id; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String productId;
        private int quantity;
        private BigDecimal price;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public OrderItemJpaEntity build() {
            OrderItemJpaEntity entity = new OrderItemJpaEntity();
            entity.id = this.id;
            entity.productId = this.productId;
            entity.quantity = this.quantity;
            entity.price = this.price;
            return entity;
        }
    }
}
package com.acme.order.infrastructure.persistence.jpa;

import com.acme.order.domain.model.OrderStatus;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "orders")
public class OrderJpaEntity implements java.io.Serializable {

    @Id
    protected UUID id;

    @Column(nullable = false)
    protected String customerId;

    @Enumerated(EnumType.STRING)
    protected OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    protected List<OrderItemJpaEntity> items;

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<OrderItemJpaEntity> getItems() {
        return items;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private UUID id;
        private String customerId;
        private OrderStatus status;
        private List<OrderItemJpaEntity> items = new ArrayList<>();
        
        private Builder() {}
        
        public Builder id(UUID id) {
            this.id = id;
            return this;
        }
        
        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }
        
        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }
        
        public Builder items(List<OrderItemJpaEntity> items) {
            this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            return this;
        }
        
        public Builder addItem(OrderItemJpaEntity item) {
            this.items.add(item);
            return this;
        }
        
        public OrderJpaEntity build() {
            OrderJpaEntity entity = new OrderJpaEntity();
            entity.id = id;
            entity.customerId = customerId;
            entity.status = status;
            entity.items = items;
            return entity;
        }
    }
}
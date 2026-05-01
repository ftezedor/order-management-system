package com.acme.order.bootstrap.spring.repository;

import com.acme.order.bootstrap.spring.BaseIntegrationTest;
import com.acme.order.domain.model.Order;
import com.acme.order.domain.model.OrderItem;
import com.acme.order.domain.ports.out.OrderRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import static org.assertj.core.api.Assertions.assertThat;

@Transactional // Add this annotation here
class OrderRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndRetrieveOrderWithItems() {

        String productId = "PROD-999";
        int quantity = 2;
        BigDecimal price = new BigDecimal("19.99");
        
        OrderItem item = new OrderItem(productId, quantity, price);

        UUID orderId = UUID.randomUUID();
        String customerId = "CUST-123";
        
        Order order = new Order(orderId, customerId, List.of(item));

        // The save operation will now run inside an active transaction
        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(orderId);
        assertThat(savedOrder.getItems()).hasSize(1);

    }
}
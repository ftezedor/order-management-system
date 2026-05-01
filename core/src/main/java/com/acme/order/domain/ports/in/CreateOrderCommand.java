package com.acme.order.domain.ports.in;

import com.acme.order.domain.model.OrderItem;
import java.util.List;

public record CreateOrderCommand(String customerId, String customerEmail, List<OrderItem> items) {

}
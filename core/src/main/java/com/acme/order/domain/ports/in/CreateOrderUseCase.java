package com.acme.order.domain.ports.in;

import com.acme.order.domain.model.Order;

public interface CreateOrderUseCase {
    Order execute(CreateOrderCommand command);
}

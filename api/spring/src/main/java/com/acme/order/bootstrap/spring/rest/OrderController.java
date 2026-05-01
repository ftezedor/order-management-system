package com.acme.order.bootstrap.spring.rest;

import com.acme.order.application.service.ListOrdersService;
import com.acme.order.domain.model.Order;
import com.acme.order.domain.ports.in.CreateOrderCommand;
import com.acme.order.domain.ports.in.CreateOrderUseCase;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableAspectJAutoProxy
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final CreateOrderUseCase createOrderUseCase;
    private final ListOrdersService getOrdersService;
    private final TransactionTemplate transactionTemplate;

    public OrderController(CreateOrderUseCase createOrderUseCase, ListOrdersService getOrdersService, 
            TransactionTemplate transactionTemplate) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrdersService = getOrdersService;
        this.transactionTemplate = transactionTemplate;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        Order order = createOrderUseCase.execute(command);
        OrderResponse response = OrderResponse.fromDomain(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<OrderResponse> getAll() {
        log.info("OrderController.getAll() called");
        // We wrap the call here so the JPA Session remains active 
        // until the Domain Mapping is finished inside the service.
        var list = transactionTemplate.execute(status -> 
            getOrdersService.execute()
        );
        
        var opt = Optional.ofNullable(list);

        log.info("OrderController.getAll() finished");

        return opt
                .orElse(List.of())
                .stream()
                .map(OrderResponse::fromDomain)
                .toList();
    }
}
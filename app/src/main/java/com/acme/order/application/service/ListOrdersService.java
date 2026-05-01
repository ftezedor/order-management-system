package com.acme.order.application.service;

import com.acme.order.application.ports.out.LogContextPort;
import com.acme.order.domain.model.Order;
import com.acme.order.domain.ports.out.OrderRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListOrdersService {

    private static final Logger log = LoggerFactory.getLogger(ListOrdersService.class);

    private final OrderRepository repository;
    private final LogContextPort logContext;

    public ListOrdersService(OrderRepository repository, LogContextPort logContext) {
        this.repository = repository;
        this.logContext = logContext;
    }

    public List<Order> execute() {
        try {
            log.info("ListOrdersService.execute() called");
            return repository.findAll();
        } finally {
            log.info("Orders had been retrieved");
            logContext.clear();
            log.info("ListOrdersService.execute() finished");
        }
    }
}
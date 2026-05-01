package com.acme.order.domain.exception;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String id) {
        super("Order with ID " + id + " not found.");
    }
}
package com.acme.order.domain.ports.out;

public interface OrderMetricsPort {
    void countOrderCreated();
    void recordOrderValidationTime(long millis);
}
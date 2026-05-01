package com.acme.order.bootstrap.spring.rest.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class OrderMetricsService {

    private final MeterRegistry meterRegistry;

    public OrderMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Initialize a counter
        this.meterRegistry.counter("order.processed.total").increment(0);
    }

    public void incrementOrderCounter() {
        // Increment the metric value
        this.meterRegistry.counter("order.processed.total").increment();
    }
}
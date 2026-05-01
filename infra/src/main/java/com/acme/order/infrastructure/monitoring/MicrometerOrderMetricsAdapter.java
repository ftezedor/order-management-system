package com.acme.order.infrastructure.monitoring;

import com.acme.order.domain.ports.out.OrderMetricsPort;
import io.micrometer.core.instrument.MeterRegistry;

public class MicrometerOrderMetricsAdapter implements OrderMetricsPort {

    private final MeterRegistry registry;

    public MicrometerOrderMetricsAdapter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void countOrderCreated() {
        registry.counter("orders.created.total").increment();
    }

    @Override
    public void recordOrderValidationTime(long millis) {
        registry.timer("orders.validation.duration").record(java.time.Duration.ofMillis(millis));
    }
}
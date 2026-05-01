package com.acme.order.bootstrap.spring.rest.config;

import com.acme.order.domain.ports.out.OrderMetricsPort;
import com.acme.order.infrastructure.monitoring.MicrometerOrderMetricsAdapter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MonitoringConfig {
    @Bean
    public OrderMetricsPort orderMetricsPort(MeterRegistry registry) {
        return new MicrometerOrderMetricsAdapter(registry);
    }
}

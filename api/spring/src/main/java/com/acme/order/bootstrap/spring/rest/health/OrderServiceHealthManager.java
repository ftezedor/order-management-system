package com.acme.order.bootstrap.spring.rest.health;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceHealthManager {

    private final ApplicationEventPublisher eventPublisher;

    public OrderServiceHealthManager(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void markServiceAsOutOfService() {
        // Blocks traffic without killing the pod (useful during heavy maintenance)
        AvailabilityChangeEvent.publish(
            eventPublisher, 
            this, 
            ReadinessState.REFUSING_TRAFFIC
        );
    }

    public void markServiceAsAcceptingTraffic() {
        AvailabilityChangeEvent.publish(
            eventPublisher, 
            this, 
            ReadinessState.ACCEPTING_TRAFFIC
        );
    }
}
package com.acme.order.bootstrap.spring.rest.scheduler;

import com.acme.order.application.ports.out.LogContextPort;
import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.infrastructure.persistence.outbox.OutboxProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;



@Component
public class OutboxRelay {

    private final OutboxProcessor processor;
    private final LogContextPort logContext;

    public OutboxRelay(OutboxProcessor processor, LogContextPort logContext) {
        this.processor = processor;
        this.logContext = logContext;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void scheduledRun() {
        processor.process();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onTransactionCommit(OrderCreatedEvent event) {
        try {
            // a new thread is created for each event so set the log context
            logContext.put("orderId", event.orderId().toString());
            processor.process();
        } finally {
            logContext.clear();
        }
    }
}

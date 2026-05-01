package com.acme.order.infrastructure.logging;

import com.acme.order.application.ports.out.LogContextPort;
import org.slf4j.MDC;


public class LogContext implements LogContextPort {
    @Override
    public void put(String key, String value) {
        MDC.put(key, value);
    }

    @Override
    public void clear() {
        MDC.clear();
    }
}
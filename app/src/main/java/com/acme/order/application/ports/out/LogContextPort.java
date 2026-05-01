package com.acme.order.application.ports.out;

public interface LogContextPort {
    void put(String key, String value);
    void clear();
}
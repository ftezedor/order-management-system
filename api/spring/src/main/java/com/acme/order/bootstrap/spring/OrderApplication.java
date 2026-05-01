package com.acme.order.bootstrap.spring;

import com.acme.order.bootstrap.spring.rest.hints.OrderRuntimeHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@ImportRuntimeHints(OrderRuntimeHints.class)
@SpringBootApplication(scanBasePackages = "com.acme.order")
/*
@EntityScan(basePackages = { "com.acme.order.infrastructure.persistence.jpa",
        "com.acme.order.bootstrap.spring", // Scan for any bootstrap entities
        "com.acme.order.infrastructure.persistence" })
@EnableJpaRepositories(basePackages = "com.acme.order.infrastructure.persistence.jpa")
*/
@EnableJpaRepositories(basePackages = "com.acme.order.infrastructure.persistence")
@EntityScan(basePackages = "com.acme.order.infrastructure.persistence")
@ComponentScan(basePackages = {"com.acme.order.bootstrap.spring", "com.acme.order.infrastructure.persistence"})
@EnableScheduling
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}

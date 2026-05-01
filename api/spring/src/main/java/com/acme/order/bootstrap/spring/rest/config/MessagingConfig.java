package com.acme.order.bootstrap.spring.rest.config;

import com.acme.order.infrastructure.messaging.RabbitMqOrderPublisher;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MessagingConfig {

    @Bean
    public Connection rabbitConnection(
            @Value("${app.rabbitmq.host}") String host,
            @Value("${app.rabbitmq.port}") int port,
            @Value("${app.rabbitmq.username}") String user,
            @Value("${app.rabbitmq.password}") String pass) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(pass);
        return factory.newConnection();
    }

    @Bean
    public RabbitMqOrderPublisher rabbitMqOrderPublisher(
            Connection connection,
            Tracer tracer,           // Autowired by Spring
            Propagator propagator,   // Autowired by Spring
            @Value("${app.rabbitmq.exchange}") String exchange) {

        return new RabbitMqOrderPublisher(connection, exchange, tracer, propagator);
        //return new RabbitMqOrderPublisher(connection, exchange, queue);
    }

}
package com.acme.order.infrastructure.messaging;

import com.acme.order.domain.event.OrderCreatedEvent;
import com.acme.order.infrastructure.persistence.outbox.OutboxEntity;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import java.util.HashMap;
import java.util.Map;




public class RabbitMqOrderPublisher {

private final Connection connection;
    private final String exchange;
    private final Tracer tracer;
    private final Propagator propagator;

    public RabbitMqOrderPublisher(Connection connection, String exchange, Tracer tracer, Propagator propagator) {
        this.connection = connection;
        this.exchange = exchange;
        this.tracer = tracer;
        this.propagator = propagator;
    }

    public void handle(OutboxEntity event) {
        publish(event.getPayload());
    }

    public void handle(OrderCreatedEvent event) {
        String json = String.format("""
            {
                "orderId": "%s",
                "email": "%s",
                "status": "%s"
            }
            """,
            event.orderId(), event.customerEmail(), event.occurredAt());
        publish(json);
    }

    /*
    private void __publish(String json) {
        try (Channel channel = connection.createChannel()) {
            // 1. Ensure the Exchange and Queue exist and are bound
            channel.exchangeDeclare(exchangeName, "topic", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, exchangeName, "order.created");

            // 3. Publish
            channel.basicPublish(exchangeName, "order.created", null,
                    json.getBytes(StandardCharsets.UTF_8));

            System.out.println(" [x] Sent to RabbitMQ: " + json);
        } catch (Exception e) {
            throw new RuntimeException("Could not publish to RabbitMQ", e);
        }
    }
    */

    public void publish(String messageJson) {
        Span publishSpan = tracer.nextSpan().name("rabbitmq publish order.created").start();
        try (Tracer.SpanInScope ignored = tracer.withSpan(publishSpan);
             Channel channel = connection.createChannel()) {
            Map<String, Object> headers = new HashMap<>();

            propagator.inject(publishSpan.context(), headers, Map::put);

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .headers(headers)
                    .contentType("application/json")
                    .build();

            channel.basicPublish(exchange, "order.created", props, messageJson.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to RabbitMQ", e);
        } finally {
            publishSpan.end();
        }
    }
}

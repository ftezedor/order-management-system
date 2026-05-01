package com.acme.order.bootstrap.spring.messaging;

import com.acme.order.bootstrap.spring.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
 class OrderMessagingIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    private final String queueName = "order-test-queue";

    @BeforeEach
    void setUp() {
        // Ensure the connection factory is ready before trying to create a queue
        amqpAdmin.declareQueue(new Queue(queueName));
    }

    @Test
    void shouldSendAndReceiveMessage() {
        String message = "{\"orderId\": \"ORD-123\", \"status\": \"CREATED\"}";

        // When
        rabbitTemplate.convertAndSend(queueName, message);

        // Then
        Object receivedMessage = rabbitTemplate.receiveAndConvert(queueName);
        assertThat(receivedMessage).isEqualTo(message);
    }
}
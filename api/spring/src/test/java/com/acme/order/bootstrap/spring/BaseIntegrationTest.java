package com.acme.order.bootstrap.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

    @SuppressWarnings("resource")
    @Container
    protected static final PostgreSQLContainer<?> postgresContainer = 
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test-orders")
            .withUsername("test-user")
            .withPassword("test-password");

    @SuppressWarnings("resource")
    @Container
    protected static final RabbitMQContainer rabbitmqContainer = 
        new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management"));

    // Static block to ensure the container is fully running and ports are mapped
    static {
        rabbitmqContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL Properties
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        // RabbitMQ Properties
        registry.add("spring.rabbitmq.host", rabbitmqContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitmqContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitmqContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitmqContainer::getAdminPassword);
    }
}
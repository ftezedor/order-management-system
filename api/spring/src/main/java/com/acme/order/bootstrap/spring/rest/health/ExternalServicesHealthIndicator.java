package com.acme.order.bootstrap.spring.rest.health;

import com.rabbitmq.client.Connection;
import javax.sql.DataSource;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ExternalServicesHealthIndicator implements HealthIndicator {

    private enum State {
        AVAILABLE,
        UNREACHABLE
    }

    private final DataSource dataSource;
    private final Connection rabbitConnection;  // RabbitMQ Connection

    public ExternalServicesHealthIndicator(DataSource dataSource, Connection rabbitConnection) {
        this.dataSource = dataSource;
        this.rabbitConnection = rabbitConnection;
    }

    @Override
    public Health health() {
        boolean dbHealthy = checkDatabaseHealth();
        boolean rabbitHealthy = checkRabbitMqHealth();

        if (dbHealthy && rabbitHealthy) {
            return Health.up()
                    .withDetail("database", State.AVAILABLE)
                    .withDetail("rabbitmq", State.AVAILABLE)
                    .build();
        }

        return Health.down()
                .withDetail("database", dbHealthy ? State.AVAILABLE : State.UNREACHABLE)
                .withDetail("rabbitmq", rabbitHealthy ? State.AVAILABLE : State.UNREACHABLE)
                .build();
    }

    private boolean checkDatabaseHealth() {
        try (java.sql.Connection connection = dataSource.getConnection();
             java.sql.Statement statement = connection.createStatement()) {
            statement.execute("SELECT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRabbitMqHealth() {
        try (com.rabbitmq.client.Channel channel = rabbitConnection.createChannel()) {
            return channel.isOpen();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
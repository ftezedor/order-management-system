# Order Management System (Hexagonal Architecture)

A professional-grade microservice demonstrating Clean Architecture principles, strict Dependency Inversion, and comprehensive Observability.

The project is designed with a "Zero-Spring" core, ensuring that the business logic (Domain and Application layers) remains independent of frameworks, databases, and messaging providers.

## 🏗️ Architectural Layers

The project follows the Hexagonal (Ports & Adapters) pattern to ensure a high degree of maintainability and testability.

1. Domain (The Core)
- Role: Contains the business entities, value objects, and pure logic.
- Constraint: Zero dependencies. No Spring, no JPA, no SLF4J.
- Key Files: Order.java, OrderItem.java, OrderStatus.java.

2. Application
- Role: Orchestrates use cases. Defines "Ports" (interfaces) for external communication.
- Zero-Framework Strategy: Uses LogContextPort and OrderRepositoryPort to interact with infrastructure without knowing the implementation details.
- Key Services: CreateOrderService, GetOrdersService.

3. Infrastructure
- Role: The "Adapters." Implements the Ports defined by the Application layer.
- Technologies: JPA/Hibernate, RabbitMQ, Micrometer Metrics.
- Persistence: Handles the Outbox Pattern (JpaOutboxAdapter) to ensure atomicity between DB state and messaging.

4. Bootstrap (API/Spring)
- Role: The "Glue." Contains the Spring Boot application, REST controllers, and dependency injection configuration.
- Responsibility: Manages transactions via TransactionTemplate and populates the MDC for tracing.

## 🔍 Observability & Tracing

This project implements a comprehensive observability stack:

- Micrometer Metrics: Custom metrics (e.g., orders.created.total) are recorded via the OrderMetricsPort.

- Distributed Tracing: Integrated with Micrometer Tracing (Brave). Every request is assigned a traceId which is propagated through RabbitMQ headers.

- MDC Logging: Logs include the traceId and business orderId in the default Spring format:
INFO [order-service,traceId,orderId] ... Message

## 🛠️ Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker (for RabbitMQ and Zipkin)

### Local Environment Setup

Start the required infrastructure:

```bash
docker run -d -p 5672:5672 -p 15672:15672 rabbitmq:3-management
docker run -d -p 9411:9411 openzipkin/zipkin
```

### Build and Run

```bash
mvn clean install
mvn spring-boot:run -pl api/spring
```


|Method|Endpoint|Description|
|------|--------|---------------|
|POST|/orders|Create a new order (Triggers Outbox + RabbitMQ)|
|GET|/orders|List all orders (Uses Join Fetch for performance)|
|GET|/actuator/prometheus|Export Micrometer metrics|
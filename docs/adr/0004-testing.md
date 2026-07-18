# ADR-0004: Integration Testing with Testcontainers

## Status
Accepted

## Context
Mock-based integration tests often fail to reproduce production behavior.

## Decision
Run integration tests against real PostgreSQL and RabbitMQ containers using Testcontainers.

## Rationale
- Higher confidence
- Production-like execution
- Repeatable tests

## Consequences

### Positive
- Better reliability
- Environment consistency

### Negative
- Docker dependency
- Longer execution time

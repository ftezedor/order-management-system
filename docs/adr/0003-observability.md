# ADR-0003: Production-first Observability

## Status
Accepted

## Context
Production systems require visibility into application behavior.

## Decision
Use Micrometer, Spring Boot Actuator, Zipkin and MDC Correlation IDs.

## Rationale
- Metrics
- Distributed tracing
- Health endpoints
- Faster troubleshooting

## Consequences

### Positive
- Better diagnostics
- Easier monitoring

### Negative
- Additional infrastructure
- Small runtime overhead

# ADR-0001: Adopt Hexagonal Architecture

## Status
Accepted

## Context
The project is intended to demonstrate enterprise backend architecture while keeping business rules independent from frameworks.

## Decision
Adopt Hexagonal Architecture (Ports & Adapters).

## Rationale
- Framework-independent domain
- Separation of concerns
- Replaceable adapters
- High testability
- Long-term maintainability

## Consequences

### Positive
- Easier testing
- Technology independence
- Clear boundaries

### Negative
- More abstractions
- Higher initial complexity

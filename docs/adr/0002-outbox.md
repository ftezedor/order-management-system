# ADR-0002: Adopt the Transactional Outbox Pattern

## Status
Accepted

## Context
Writing to the database and publishing a message independently may create dual-write inconsistencies.

## Decision
Persist events in an Outbox table within the same transaction and publish them asynchronously.

## Rationale
- Reliable event publication
- Atomic updates
- Retry capability

## Consequences

### Positive
- Prevents lost events
- Improves resilience

### Negative
- Extra persistence table
- Background publisher required

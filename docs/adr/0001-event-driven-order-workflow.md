# ADR 0001: Event-driven payment workflow

- Status: Accepted
- Date: 2026-09-16

## Context

Order creation must not hold an HTTP connection open while a payment provider completes work. Kafka is already part of the target platform.

## Decision

Reserve stock synchronously, then request payment asynchronously through Kafka. Store the payment request in an order-side transactional outbox before publishing it.

## Consequences

- The API returns `PENDING_PAYMENT`, so clients must poll or later use notifications.
- Payment and order consumers must be idempotent because delivery is at least once.
- Operational visibility must include outbox age, consumer lag, and failure counts.
- A payment failure requires a compensating inventory-release command in the next Saga milestone.

# Architecture

## Goals

Cloud Commerce Platform is a compact reference system for practicing Java microservices delivery in Docker, Kubernetes, and GitOps environments. It favors explicit boundaries and failure behavior over feature breadth.

## Boundaries

- Product owns catalog data.
- Inventory owns availability and reservations.
- Order owns the customer-visible workflow.
- Payment owns payment attempts and outcomes.
- The gateway is routing infrastructure, not a business-logic layer.

No service reads another service's database.

## Consistency model

The order workflow mixes synchronous and asynchronous coordination:

1. The order service reserves stock synchronously so it can reject unavailable items immediately.
2. The order and a `PaymentRequested` outbox record commit in one database transaction.
3. The outbox worker publishes the request to Kafka with at-least-once semantics.
4. Payment uses a unique `order_id`, making duplicate requests harmless.
5. Payment publishes `PaymentCompleted`; order applies the result only while pending.

This trades immediate workflow simplicity for one documented gap: a failed payment currently needs an inventory-release compensation. That step is retained for the Saga milestone rather than hidden.

## Failure behavior

| Failure | Behavior | Recovery |
|---|---|---|
| Inventory unavailable | New order fails before it is accepted | Client retries with the same idempotency key |
| Kafka unavailable | Outbox row remains unpublished | Scheduled publisher retries |
| Payment service restarts | Kafka retains the request | Consumer resumes from committed offset |
| Duplicate payment message | Existing payment is reused | Completion event may be re-emitted safely |
| Order service restarts | Order state remains durable | Completion event is consumed after restart |

## Deployment model

Docker Compose is the local integration environment. Helm represents the cluster deployment. PostgreSQL and Kafka are deliberately external to the application chart so stateful platform choices remain independent.

## Security model

Current controls include non-root containers, Kubernetes restricted security context, no committed secrets, input validation, and a default-deny ingress policy. OIDC, authorization scopes, TLS/mTLS, signed images, and runtime policy enforcement are roadmap items.

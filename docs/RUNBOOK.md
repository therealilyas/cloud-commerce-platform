# Operations runbook

## Service not ready

1. Check `docker compose ps` or `kubectl get pods -n commerce`.
2. Read the failing service logs.
3. Query `/actuator/health/readiness` and identify the failing component.
4. Confirm database DNS, credentials, and Kafka bootstrap address.
5. Restart only after capturing the error and checking dependency health.

## Orders stuck in PENDING_PAYMENT

1. Check Redpanda/Kafka health.
2. Inspect unpublished rows in `orders.outbox_events`.
3. Check payment consumer lag and payment-service logs.
4. Confirm a payment row exists for the order.
5. Replaying is safe: order and payment handlers are idempotent.

## Inventory mismatch

1. Stop new order traffic.
2. Compare stock reservations with affected orders.
3. Do not edit counts until the source of truth and incident window are known.
4. Restore from backup or apply an audited correction.
5. Add a regression test and document the incident.

## Recovery objectives

The homelab target is RPO 15 minutes and RTO 60 minutes. These are goals until a backup/restore drill records evidence.

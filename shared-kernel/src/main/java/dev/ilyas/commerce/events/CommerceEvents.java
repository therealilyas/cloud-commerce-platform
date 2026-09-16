package dev.ilyas.commerce.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class CommerceEvents {
    private CommerceEvents() {}

    public record PaymentRequested(
            UUID eventId, UUID orderId, UUID customerId, BigDecimal amount,
            String idempotencyKey, Instant occurredAt) {}

    public record PaymentCompleted(
            UUID eventId, UUID orderId, String paymentId, String status,
            String reason, Instant occurredAt) {}
}

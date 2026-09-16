package dev.ilyas.commerce.payment;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = "order_id"))
public class Payment {
    @Id private UUID id;
    @Column(name = "order_id", nullable = false) private UUID orderId;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Column(nullable = false) private String status;
    @Column(length = 500) private String reason;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    protected Payment() {}
    Payment(UUID orderId, BigDecimal amount, String status, String reason) {
        this.id = UUID.randomUUID(); this.orderId = orderId; this.amount = amount;
        this.status = status; this.reason = reason; this.createdAt = Instant.now();
    }
    UUID getId() { return id; }
    UUID getOrderId() { return orderId; }
    String getStatus() { return status; }
    String getReason() { return reason; }
}

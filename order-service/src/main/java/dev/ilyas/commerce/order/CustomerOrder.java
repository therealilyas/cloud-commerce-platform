package dev.ilyas.commerce.order;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(name = "uk_order_idempotency", columnNames = "idempotency_key"))
public class CustomerOrder {
    @Id private UUID id;
    @Column(name = "customer_id", nullable = false) private UUID customerId;
    @Column(name = "product_id", nullable = false) private UUID productId;
    @Column(nullable = false) private int quantity;
    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2) private BigDecimal unitPrice;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal total;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private OrderStatus status;
    @Column(name = "idempotency_key", nullable = false, length = 128) private String idempotencyKey;
    @Column(name = "failure_reason", length = 500) private String failureReason;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;

    protected CustomerOrder() {}
    CustomerOrder(UUID customerId, UUID productId, int quantity, BigDecimal unitPrice, String idempotencyKey) {
        this.id = UUID.randomUUID(); this.customerId = customerId; this.productId = productId;
        this.quantity = quantity; this.unitPrice = unitPrice; this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
        this.status = OrderStatus.PENDING_PAYMENT; this.idempotencyKey = idempotencyKey;
        this.createdAt = Instant.now(); this.updatedAt = createdAt;
    }
    void applyPayment(String paymentStatus, String reason) {
        if (status != OrderStatus.PENDING_PAYMENT) return;
        status = "SUCCEEDED".equals(paymentStatus) ? OrderStatus.CONFIRMED : OrderStatus.PAYMENT_FAILED;
        failureReason = reason; updatedAt = Instant.now();
    }
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public UUID getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotal() { return total; }
    public OrderStatus getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}

enum OrderStatus { PENDING_PAYMENT, CONFIRMED, PAYMENT_FAILED, CANCELLED }

@Entity
@Table(name = "outbox_events")
class OutboxEvent {
    @Id UUID id;
    @Column(name = "aggregate_id", nullable = false) UUID aggregateId;
    @Column(name = "event_type", nullable = false) String eventType;
    @Column(nullable = false, columnDefinition = "TEXT") String payload;
    @Column(name = "created_at", nullable = false) Instant createdAt;
    @Column(name = "published_at") Instant publishedAt;
    protected OutboxEvent() {}
    OutboxEvent(UUID aggregateId, String eventType, String payload) {
        this.id = UUID.randomUUID(); this.aggregateId = aggregateId; this.eventType = eventType;
        this.payload = payload; this.createdAt = Instant.now();
    }
    void markPublished() { this.publishedAt = Instant.now(); }
}

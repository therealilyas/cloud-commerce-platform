package dev.ilyas.commerce.inventory;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id private UUID productId;
    @Column(nullable = false) private int available;
    @Version private long version;

    protected Inventory() {}
    Inventory(UUID productId, int available) { this.productId = productId; this.available = available; }
    void add(int quantity) { this.available += quantity; }
    void reserve(int quantity) {
        if (quantity <= 0 || available < quantity) throw new InsufficientStockException(productId, available, quantity);
        available -= quantity;
    }
    public UUID getProductId() { return productId; }
    public int getAvailable() { return available; }
}

@Entity
@Table(name = "stock_reservations", uniqueConstraints = @UniqueConstraint(columnNames = "order_id"))
class StockReservation {
    @Id private UUID id;
    @Column(name = "order_id", nullable = false) private UUID orderId;
    @Column(name = "product_id", nullable = false) private UUID productId;
    @Column(nullable = false) private int quantity;

    protected StockReservation() {}
    StockReservation(UUID orderId, UUID productId, int quantity) {
        this.id = UUID.randomUUID(); this.orderId = orderId; this.productId = productId; this.quantity = quantity;
    }
    UUID getOrderId() { return orderId; }
    UUID getProductId() { return productId; }
    int getQuantity() { return quantity; }
}

class InsufficientStockException extends RuntimeException {
    InsufficientStockException(UUID productId, int available, int requested) {
        super("Insufficient stock for product %s: available=%d requested=%d".formatted(productId, available, requested));
    }
}

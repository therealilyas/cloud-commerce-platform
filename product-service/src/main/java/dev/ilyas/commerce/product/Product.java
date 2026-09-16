package dev.ilyas.commerce.product;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(name = "uk_product_sku", columnNames = "sku"))
public class Product {
    @Id private UUID id;
    @Column(nullable = false, length = 64) private String sku;
    @Column(nullable = false, length = 160) private String name;
    @Column(nullable = false, length = 1000) private String description;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal price;
    @Column(nullable = false) private boolean active;
    @Column(nullable = false, updatable = false) private Instant createdAt;

    protected Product() {}

    public Product(String sku, String name, String description, BigDecimal price) {
        this.id = UUID.randomUUID();
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = true;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
}

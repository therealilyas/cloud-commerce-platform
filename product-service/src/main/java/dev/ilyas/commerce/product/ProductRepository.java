package dev.ilyas.commerce.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySku(String sku);
}

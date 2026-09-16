package dev.ilyas.commerce.inventory;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Inventory i where i.productId = :productId")
    Optional<Inventory> findForUpdate(@Param("productId") UUID productId);
}

interface StockReservationRepository extends JpaRepository<StockReservation, UUID> {
    Optional<StockReservation> findByOrderId(UUID orderId);
}

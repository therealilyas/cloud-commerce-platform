package dev.ilyas.commerce.inventory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class InventoryService {
    private final InventoryRepository inventory;
    private final StockReservationRepository reservations;

    public InventoryService(InventoryRepository inventory, StockReservationRepository reservations) {
        this.inventory = inventory; this.reservations = reservations;
    }

    @Transactional
    public Inventory add(UUID productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        var item = inventory.findForUpdate(productId).orElseGet(() -> new Inventory(productId, 0));
        item.add(quantity);
        return inventory.save(item);
    }

    @Transactional
    public ReservationResult reserve(UUID orderId, UUID productId, int quantity) {
        return reservations.findByOrderId(orderId)
                .map(r -> new ReservationResult(r.getOrderId(), r.getProductId(), r.getQuantity(), true))
                .orElseGet(() -> {
                    var item = inventory.findForUpdate(productId)
                            .orElseThrow(() -> new InsufficientStockException(productId, 0, quantity));
                    item.reserve(quantity);
                    reservations.save(new StockReservation(orderId, productId, quantity));
                    return new ReservationResult(orderId, productId, quantity, false);
                });
    }

    public record ReservationResult(UUID orderId, UUID productId, int quantity, boolean replayed) {}
}

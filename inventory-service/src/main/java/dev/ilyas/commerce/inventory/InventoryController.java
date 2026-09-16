package dev.ilyas.commerce.inventory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService service;
    private final InventoryRepository repository;

    public InventoryController(InventoryService service, InventoryRepository repository) {
        this.service = service; this.repository = repository;
    }

    @GetMapping("/{productId}")
    public StockResponse stock(@PathVariable UUID productId) {
        return repository.findById(productId).map(i -> new StockResponse(i.getProductId(), i.getAvailable()))
                .orElse(new StockResponse(productId, 0));
    }

    @PostMapping("/{productId}/stock")
    public StockResponse add(@PathVariable UUID productId, @Valid @RequestBody AddStockRequest request) {
        var i = service.add(productId, request.quantity());
        return new StockResponse(i.getProductId(), i.getAvailable());
    }

    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryService.ReservationResult reserve(@Valid @RequestBody ReserveRequest request) {
        return service.reserve(request.orderId(), request.productId(), request.quantity());
    }

    record AddStockRequest(@Positive int quantity) {}
    record ReserveRequest(@NotNull UUID orderId, @NotNull UUID productId, @Positive int quantity) {}
    record StockResponse(UUID productId, int available) {}

    @ExceptionHandler(InsufficientStockException.class)
    ResponseEntity<Map<String, Object>> insufficient(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "INSUFFICIENT_STOCK", "message", ex.getMessage()));
    }
}

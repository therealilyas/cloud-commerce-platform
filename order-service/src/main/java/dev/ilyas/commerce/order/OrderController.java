package dev.ilyas.commerce.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderWorkflow workflow;
    OrderController(OrderWorkflow workflow) { this.workflow = workflow; }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    OrderWorkflow.OrderResult create(@RequestHeader("Idempotency-Key") @Size(min = 8, max = 128) String key,
                                     @Valid @RequestBody CreateOrderRequest request) {
        return workflow.create(new OrderWorkflow.CreateOrder(request.customerId(), request.productId(),
                request.quantity(), request.unitPrice()), key);
    }

    @GetMapping("/{id}")
    OrderWorkflow.OrderResult one(@PathVariable UUID id) { return workflow.get(id); }

    record CreateOrderRequest(@NotNull UUID customerId, @NotNull UUID productId,
                              @Positive int quantity, @NotNull @DecimalMin("0.01") BigDecimal unitPrice) {}

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(OrderNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", "ORDER_NOT_FOUND", "message", ex.getMessage()));
    }
}

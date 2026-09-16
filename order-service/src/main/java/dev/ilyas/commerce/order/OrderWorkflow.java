package dev.ilyas.commerce.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ilyas.commerce.events.CommerceEvents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class OrderWorkflow {
    private final OrderRepository orders;
    private final OutboxRepository outbox;
    private final RestClient inventoryClient;
    private final ObjectMapper json;

    OrderWorkflow(OrderRepository orders, OutboxRepository outbox, RestClient.Builder builder,
                  ObjectMapper json, @Value("${clients.inventory.base-url}") String inventoryUrl) {
        this.orders = orders; this.outbox = outbox; this.inventoryClient = builder.baseUrl(inventoryUrl).build(); this.json = json;
    }

    @Transactional
    public OrderResult create(CreateOrder command, String idempotencyKey) {
        return orders.findByIdempotencyKey(idempotencyKey).map(OrderResult::from).orElseGet(() -> {
            var order = new CustomerOrder(command.customerId(), command.productId(), command.quantity(), command.unitPrice(), idempotencyKey);
            inventoryClient.post().uri("/api/inventory/reservations")
                    .body(new ReserveStock(order.getId(), command.productId(), command.quantity()))
                    .retrieve().toBodilessEntity();
            orders.save(order);
            var event = new CommerceEvents.PaymentRequested(UUID.randomUUID(), order.getId(), command.customerId(),
                    order.getTotal(), idempotencyKey, Instant.now());
            try {
                outbox.save(new OutboxEvent(order.getId(), "PaymentRequested", json.writeValueAsString(event)));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Could not serialize payment request", e);
            }
            return OrderResult.from(order);
        });
    }

    public OrderResult get(UUID id) {
        return orders.findById(id).map(OrderResult::from).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @KafkaListener(topics = "payment.completed", groupId = "order-service")
    @Transactional
    public void onPaymentCompleted(CommerceEvents.PaymentCompleted event) {
        orders.findById(event.orderId()).ifPresent(order -> order.applyPayment(event.status(), event.reason()));
    }

    public record CreateOrder(UUID customerId, UUID productId, int quantity, BigDecimal unitPrice) {}
    record ReserveStock(UUID orderId, UUID productId, int quantity) {}
    public record OrderResult(UUID id, UUID customerId, UUID productId, int quantity, BigDecimal unitPrice,
                              BigDecimal total, String status, String failureReason, Instant createdAt, Instant updatedAt) {
        static OrderResult from(CustomerOrder o) {
            return new OrderResult(o.getId(), o.getCustomerId(), o.getProductId(), o.getQuantity(), o.getUnitPrice(),
                    o.getTotal(), o.getStatus().name(), o.getFailureReason(), o.getCreatedAt(), o.getUpdatedAt());
        }
    }
}

class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(UUID id) { super("Order not found: " + id); }
}

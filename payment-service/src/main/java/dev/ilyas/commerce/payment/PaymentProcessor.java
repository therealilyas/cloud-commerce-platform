package dev.ilyas.commerce.payment;

import dev.ilyas.commerce.events.CommerceEvents;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByOrderId(UUID orderId);
}

@Component
public class PaymentProcessor {
    private final PaymentRepository payments;
    private final KafkaTemplate<String, Object> kafka;
    private final double successRate;

    PaymentProcessor(PaymentRepository payments, KafkaTemplate<String, Object> kafka,
                     @Value("${payment.success-rate:0.90}") double successRate) {
        this.payments = payments; this.kafka = kafka; this.successRate = successRate;
    }

    @KafkaListener(topics = "payment.requested", groupId = "payment-service")
    @Transactional
    public void process(CommerceEvents.PaymentRequested request) {
        var payment = payments.findByOrderId(request.orderId()).orElseGet(() -> {
            boolean success = ThreadLocalRandom.current().nextDouble() < successRate;
            return payments.save(new Payment(request.orderId(), request.amount(), success ? "SUCCEEDED" : "FAILED",
                    success ? null : "Simulated payment decline"));
        });
        var result = new CommerceEvents.PaymentCompleted(UUID.randomUUID(), payment.getOrderId(),
                payment.getId().toString(), payment.getStatus(), payment.getReason(), Instant.now());
        kafka.send("payment.completed", payment.getOrderId().toString(), result);
    }
}

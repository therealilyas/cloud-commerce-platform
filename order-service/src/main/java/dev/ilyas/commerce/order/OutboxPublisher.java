package dev.ilyas.commerce.order;

import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;

@Component
class OutboxPublisher {
    private final OutboxRepository outbox;
    private final KafkaTemplate<String, String> kafka;
    OutboxPublisher(OutboxRepository outbox, KafkaTemplate<String, String> kafka) { this.outbox = outbox; this.kafka = kafka; }

    @Scheduled(fixedDelayString = "${outbox.publish-delay-ms:1000}")
    @Transactional
    public void publish() {
        for (var event : outbox.findByPublishedAtIsNullOrderByCreatedAtAsc(PageRequest.of(0, 50))) {
            kafka.send("payment.requested", event.aggregateId.toString(), event.payload)
                    .orTimeout(Duration.ofSeconds(5).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS).join();
            event.markPublished();
        }
    }
}

package dev.ilyas.commerce.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface OrderRepository extends JpaRepository<CustomerOrder, UUID> {
    Optional<CustomerOrder> findByIdempotencyKey(String idempotencyKey);
}

interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    List<OutboxEvent> findByPublishedAtIsNullOrderByCreatedAtAsc(Pageable pageable);
}

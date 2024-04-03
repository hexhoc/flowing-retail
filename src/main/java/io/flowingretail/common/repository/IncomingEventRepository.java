package io.flowingretail.common.repository;

import io.flowingretail.common.entity.IncomingEvent;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingEventRepository extends JpaRepository<IncomingEvent, UUID> {
    Optional<IncomingEvent> findByTraceId(UUID traceId);
    Optional<IncomingEvent> findByCorrelationId(UUID correlationId);
}

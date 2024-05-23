package io.flowingretail.common.adapter.out.jpa;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingEventRepository extends JpaRepository<IncomingEventEntity, UUID> {
    Optional<IncomingEventEntity> findByTraceId(UUID traceId);
    Optional<IncomingEventEntity> findByCorrelationId(UUID correlationId);
}

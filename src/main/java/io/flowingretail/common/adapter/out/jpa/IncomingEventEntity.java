package io.flowingretail.common.adapter.out.jpa;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "incoming_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingEventEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "trace_id", columnDefinition = "uuid", nullable = false)
    private UUID traceId;

    @Column(name = "correlation_id", columnDefinition = "uuid", nullable = false)
    private UUID correlationId;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Type(JsonBinaryType.class)
    @Column(name = "request", nullable = false, columnDefinition = "jsonb")
    private String request; // Assuming the request is a JSON String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}

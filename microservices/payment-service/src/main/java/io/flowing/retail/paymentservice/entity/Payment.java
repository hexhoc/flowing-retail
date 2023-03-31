package io.flowing.retail.paymentservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.flowing.retail.paymentservice.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "date")
    @NotNull
    private LocalDateTime date;

    @Column(name = "order_id")
    @NotNull
    private String orderId;

    @Column(name = "customer_id")
    @NotNull
    private Integer customerId;

    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "modified_date", nullable = false)
    @UpdateTimestamp
    private Timestamp modifiedDate;

    @Column(name = "version")
    @Version
    private Long version;

}
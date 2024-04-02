package io.flowingretail.shippingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "waybills")
@Getter
@Setter
public class Waybill {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "uuid DEFAULT gen_random_uuid()", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "waybill_date")
    @NotNull
    private LocalDateTime waybillDate;

    @Column(name = "order_id")
    @NotNull
    private String orderId;

    @Column(name = "recipient_name")
    @NotNull
    private String recipientName;

    @Column(name = "recipient_address")
    @NotNull
    private String recipientAddress;

    @Column(name = "logistics_provider")
    @NotNull
    private String logisticsProvider;

}
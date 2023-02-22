package io.flowing.retail.shippingservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "waybills")
@Getter
@Setter
public class Waybill {

    @Id
    @Column(name = "id")
    private Integer id;

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
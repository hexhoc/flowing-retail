package io.flowing.retail.paymentservice.dto;

import io.flowing.retail.paymentservice.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Payment data transfer object. Used for request and response")
public class PaymentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Payment id")
    @NotNull
    private String id;

    @Schema(description = "Payment date")
    @NotNull
    private LocalDateTime date;

    @Schema(description = "Order id")
    @NotNull
    private Integer orderId;

    @Schema(description = "Customer id")
    @NotNull
    private Integer customerId;

    @Schema(description = "Payment type (0 - incoming, 1 - outgoing)")
    @NotNull
    private PaymentType type;

    @Schema(description = "Payment amount")
    @NotNull
    private BigDecimal amount;

    private Boolean isDeleted = false;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private Long version;
}

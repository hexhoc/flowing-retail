package io.flowing.retail.shippingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "Waybill data transfer object. Used for request and response")
public class WaybillDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "waybill date")
    private LocalDateTime waybillDate;

    @Schema(description = "Order id")
    private Integer orderId;

    @Schema(description = "recipient name")
    private String recipientName;

    @Schema(description = "recipient address")
    private String recipientAddress;

    @Schema(description = "logistics provider")
    private String logisticsProvider;
}

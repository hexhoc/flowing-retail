package io.flowingretail.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Data;

@Data
@Schema(description = "Order information")
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "customer id")
    private Integer customerId;

    @Schema(description = "address")
    private String address;

    @Schema(description = "Order status", example = "NEW, CANCELLED, PAID, DELIVERED")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    @Schema(description = "total order price")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPrice;

    @Schema(description = "is deleted")
    private Boolean isDeleted = false;

    @Schema(description = "order items")
    private Set<OrderItemDTO> orderItems = new LinkedHashSet<>();

}

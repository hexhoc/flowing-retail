package io.flowing.retail.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Null;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Schema(description = "Order information")
public class OrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "order no")
    private String orderNo;

    @Schema(description = "order date")
    private LocalDateTime orderDate;

    @Schema(description = "customer id")
    private Integer customerId;

    @Schema(description = "address")
    private String address;

    @Schema(description = "Order status", example = "NEW, CANCELLED, PAID, DELIVERED")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Short status;

    @Schema(description = "total order price")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalPrice;

    @Schema(description = "is deleted")
    private Boolean isDeleted;

    @Schema(description = "created date")
    private LocalDateTime createdDate;

    @Schema(description = "modified date")
    private LocalDateTime modifiedDate;

    @Schema(description = "version")
    private Long version;

    @Schema(description = "order items")
    private Set<OrderItemDTO> orderItems = new LinkedHashSet<>();

}

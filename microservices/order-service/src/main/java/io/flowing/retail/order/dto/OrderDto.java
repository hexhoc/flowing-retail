package io.flowing.retail.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Order information")
public class OrderDto {

    @Schema(description = "Order id", required = true)
    @NotBlank
    private String orderId = "checkout-generated-" + UUID.randomUUID().toString();

    @Schema(description = "Customer information")
    private CustomerDto customer;

    @Schema(description = "Order status", example = "NEW, CANCELLED, PAID, DELIVERED")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderStatus;

    // TODO: Change Integer to BigDecimal
    @Schema(description = "Total order sum")
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer totalSum;

    @Schema(description = "Order's items")
    private List<OrderItemDto> items = new ArrayList<>();

    public void addItem(String articleId, int amount) {
        // keep only one item, but increase amount accordingly
        OrderItemDto existingItemDto = removeItem(articleId);
        if (existingItemDto != null) {
            amount += existingItemDto.getAmount();
        }

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setAmount(amount);
        itemDto.setArticleId(articleId);
        items.add(itemDto);
    }

    public OrderItemDto removeItem(String articleId) {
        for (OrderItemDto itemDto : items) {
            if (articleId.equals(itemDto.getArticleId())) {
                items.remove(itemDto);
                return itemDto;
            }
        }
        return null;
    }

}

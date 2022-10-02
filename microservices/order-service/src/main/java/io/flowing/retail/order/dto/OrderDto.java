package io.flowing.retail.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {

    private String orderId = "checkout-generated-" + UUID.randomUUID().toString();
    private CustomerDto customer;
    @Null
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String orderStatus;
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

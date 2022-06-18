package io.flowing.retail.order.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {
  
  private String orderId = "checkout-generated-" + UUID.randomUUID().toString();
  private CustomerDto customerDto;
  private List<OrderItemDto> itemDtoList = new ArrayList<>();
  
  public void addItem(String articleId, int amount) {
    // keep only one item, but increase amount accordingly
    OrderItemDto existingItemDto = removeItem(articleId);
    if (existingItemDto !=null) {
      amount += existingItemDto.getAmount();
    }
    
    OrderItemDto itemDto = new OrderItemDto();
    itemDto.setAmount(amount);
    itemDto.setArticleId(articleId);
    itemDtoList.add(itemDto);
  }

  public OrderItemDto removeItem(String articleId) {
    for (OrderItemDto itemDto : itemDtoList) {
      if (articleId.equals(itemDto.getArticleId())) {
        itemDtoList.remove(itemDto);
        return itemDto;
      }
    }
    return null;
  }
  
}

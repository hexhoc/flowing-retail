package io.flowing.retail.inventory.messages.payload;

import java.util.ArrayList;
import java.util.List;

import io.flowing.retail.inventory.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FetchGoodsCommandPayload {
  
  private String refId;
  private String reason = "CustomerOrder";
  private List<Item> items = new ArrayList<>();

}

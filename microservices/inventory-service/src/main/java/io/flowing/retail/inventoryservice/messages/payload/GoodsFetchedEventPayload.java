package io.flowing.retail.inventoryservice.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodsFetchedEventPayload {
  
  private String refId;
  private String pickId;

}

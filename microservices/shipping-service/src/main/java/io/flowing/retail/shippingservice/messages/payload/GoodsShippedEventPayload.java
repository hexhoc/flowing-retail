package io.flowing.retail.shippingservice.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodsShippedEventPayload {
  
  private String refId;
  private String shipmentId;

}

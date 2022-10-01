package io.flowing.retail.shipping.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipGoodsCommandPayload {
  
  private String refId;
  private String pickId;
  private String logisticsProvider;
  private String recipientName;
  private String recipientAddress;

}

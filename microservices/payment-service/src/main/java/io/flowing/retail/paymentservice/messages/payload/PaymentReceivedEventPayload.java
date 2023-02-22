package io.flowing.retail.paymentservice.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReceivedEventPayload {
  private String refId;
  private String shipmentId;
}

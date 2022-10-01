package io.flowing.retail.payment.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReceivedEventPayload {
  private String refId;

}

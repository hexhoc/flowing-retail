package io.flowing.retail.payment.messages.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrievePaymentCommandPayload {
  // Order id
  private String refId;
  private String reason;
  private int amount;

}

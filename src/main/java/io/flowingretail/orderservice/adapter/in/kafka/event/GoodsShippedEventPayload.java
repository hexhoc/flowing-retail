package io.flowingretail.orderservice.adapter.in.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodsShippedEventPayload {
  private String refId;
  private String shipmentId;
}
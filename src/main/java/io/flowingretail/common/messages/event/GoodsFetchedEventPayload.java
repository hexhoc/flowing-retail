package io.flowingretail.common.messages.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodsFetchedEventPayload {
  private String refId;
  private String pickId;
}

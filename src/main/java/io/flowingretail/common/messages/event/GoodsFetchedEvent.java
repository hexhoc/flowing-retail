package io.flowingretail.common.messages.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GoodsFetchedEvent extends ApplicationEvent {
  private String payload;

  /**
   * Construct an instance with the provided source and Kafka event.
   *
   * @param source  the container instance that generated the event
   * @param payload event
   */
  public GoodsFetchedEvent(Object source, String payload) {
    super(source);
    this.payload = payload;
  }
}

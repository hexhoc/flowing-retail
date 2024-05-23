package io.flowingretail.orderservice.adapter.in.kafka;

import static io.flowingretail.common.constants.EventTypeConstants.GOODS_FETCHED_EVENT;
import static io.flowingretail.common.constants.EventTypeConstants.GOODS_SHIPPED_EVENT;
import static io.flowingretail.common.constants.EventTypeConstants.PAYMENT_RECEIVED_EVENT;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsFetchedEvent;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsShippedEvent;
import io.flowingretail.orderservice.adapter.in.kafka.event.PaymentReceivedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;

@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private final ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "order", topics = {KafkaConfig.ORDER_TOPIC})
  public void orderEventListener(String messagePayloadJson,
                                 @Header("type") String eventType) {
    log.info(eventType);

    if (PAYMENT_RECEIVED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new PaymentReceivedEvent(this, messagePayloadJson));
    } else if (GOODS_FETCHED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new GoodsFetchedEvent(this, messagePayloadJson));
    } else if (GOODS_SHIPPED_EVENT.equals(eventType)) {
      applicationEventPublisher.publishEvent(new GoodsShippedEvent(this, messagePayloadJson));
    } else {
      log.info("Ignored message of type " + eventType);
    }
  }
}
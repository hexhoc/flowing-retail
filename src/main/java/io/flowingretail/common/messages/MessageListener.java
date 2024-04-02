package io.flowingretail.common.messages;

import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.command.FetchGoodsCommand;
import io.flowingretail.common.messages.command.RetrievePaymentCommand;
import io.flowingretail.common.messages.command.ShipGoodsCommand;
import io.flowingretail.common.messages.event.GoodsFetchedEvent;
import io.flowingretail.common.messages.event.GoodsShippedEvent;
import io.flowingretail.common.messages.event.PaymentReceivedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageListener {
  private ApplicationEventPublisher applicationEventPublisher;

  @KafkaListener(id = "order", topics = {KafkaConfig.ORDER_TOPIC})
  public void orderEventListener(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    log.info(messageType);

    if ("PaymentReceivedEvent".equals(messageType)) {
      applicationEventPublisher.publishEvent(new PaymentReceivedEvent(this, messagePayloadJson));
    } else if ("GoodsFetchedEvent".equals(messageType)) {
      applicationEventPublisher.publishEvent(new GoodsFetchedEvent(this, messagePayloadJson));
    } else if ("GoodsShippedEvent".equals(messageType)) {
      applicationEventPublisher.publishEvent(new GoodsShippedEvent(this, messagePayloadJson));
    } else {
      log.info("Ignored message of type " + messageType);
    }
  }

  @KafkaListener(id = "inventory", topics = KafkaConfig.INVENTORY_TOPIC)
  public void inventoryEventReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    if (!"FetchGoodsCommand".equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }
    applicationEventPublisher.publishEvent(new FetchGoodsCommand(this, messagePayloadJson));
  }

  @KafkaListener(id = "payment", topics = KafkaConfig.PAYMENT_TOPIC)
  public void paymentEventReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    if (!"RetrievePaymentCommand".equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }
    applicationEventPublisher.publishEvent(new RetrievePaymentCommand(this, messagePayloadJson));
  }

  @Transactional
  @KafkaListener(id = "shipping", topics = KafkaConfig.SHIPMENT_TOPIC)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    if (!"ShipGoodsCommand".equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }
    applicationEventPublisher.publishEvent(new ShipGoodsCommand(this, messagePayloadJson));
  }
}
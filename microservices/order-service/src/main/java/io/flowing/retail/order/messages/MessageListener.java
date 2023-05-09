package io.flowing.retail.order.messages;

import java.util.Collections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.order.config.KafkaConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import io.flowing.retail.order.messages.event.GoodsFetchedEventPayload;
import io.flowing.retail.order.messages.event.GoodsShippedEventPayload;
import io.flowing.retail.order.messages.event.PaymentReceivedEventPayload;
import io.flowing.retail.order.service.OrderService;

@Component
@RequiredArgsConstructor
@Log
public class MessageListener {
  private final ObjectMapper objectMapper;
  private final OrderService orderService;

  @KafkaListener(id = "order", topics = {KafkaConfig.PAYMENT_TOPIC, KafkaConfig.INVENTORY_TOPIC, KafkaConfig.SHIPMENT_TOPIC})
  public void orderEventListener(String messagePayloadJson, @Header("type") String messageType) throws Exception {
    log.info(messageType);
    if ("PaymentReceivedEvent".equals(messageType)) {
      paymentReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    } else if ("GoodsFetchedEvent".equals(messageType)) {
      goodsFetchedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    } else if ("GoodsShippedEvent".equals(messageType)) {
      goodsShippedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    } else {
      log.info("Ignored message of type " + messageType);
    }
  }

  public void paymentReceived(Message<PaymentReceivedEventPayload> message) {
    log.info("paymentReceived");

    // TODO: Check. Payment success or not
    PaymentReceivedEventPayload paymentReceivedEventPayload = message.getData();
    orderService.changeStatus(paymentReceivedEventPayload.getRefId());

//    log.info(message.getType());
//    log.info(message.getCorrelationid());
    log.info("Payment id: " + paymentReceivedEventPayload.getPaymentId());
    log.info("Correlated " + message);
  }

  public void goodsFetchedReceived(Message<GoodsFetchedEventPayload> message) {
    log.info("goodsFetchedReceived");

    // TODO: Check. Fetched success or not
    GoodsFetchedEventPayload goodsFetchedEventPayload = message.getData();
    orderService.changeStatus(goodsFetchedEventPayload.getRefId());

    log.info("pick id: " + goodsFetchedEventPayload.getPickId());
    log.info("Correlated " + message);
  }

  public void goodsShippedReceived(Message<GoodsShippedEventPayload> message) {
    log.info("goodsShippedReceived");

    // TODO: Check. Shipment success or not
    GoodsShippedEventPayload goodsShippedEventPayload = message.getData();
    orderService.changeStatus(goodsShippedEventPayload.getRefId());
    log.info("shipment id: " + goodsShippedEventPayload.getShipmentId());
    log.info("Correlated " + message);
  }
}

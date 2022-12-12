package io.flowing.retail.order.messages;

import java.util.Collections;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.process.payload.GoodsFetchedEventPayload;
import io.flowing.retail.order.process.payload.GoodsShippedEventPayload;
import io.flowing.retail.order.process.payload.PaymentReceivedEventPayload;
import io.flowing.retail.order.repository.OrderRepository;

@Component
@RequiredArgsConstructor
public class MessageListener {
  private final ZeebeClient zeebe;
  private final ObjectMapper objectMapper;

  @KafkaListener(id = "order", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{

    if ("PaymentReceivedEvent".equals(messageType)) {
      paymentReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    }
    else if ("GoodsFetchedEvent".equals(messageType)) {
      goodsFetchedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    }
    else if ("GoodsShippedEvent".equals(messageType)) {
      goodsShippedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<>() {}));
    }
    else {
      System.out.println("Ignored message of type " + messageType );
    }
  }

  @Transactional
  public void paymentReceived(Message<PaymentReceivedEventPayload> message) {
     // Here you would maybe we should read something from the payload:
    message.getData();

    zeebe.newPublishMessageCommand() //
      .messageName(message.getType())
      .correlationKey(message.getCorrelationid())
      .variables(Collections.singletonMap("paymentInfo", "YeahWeCouldAddSomething"))
      .send().join();  

    System.out.println("Correlated " + message);
  }

  @Transactional
  public void goodsFetchedReceived(Message<GoodsFetchedEventPayload> message) {
    String pickId = message.getData().getPickId();     

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) // 
        .variables(Collections.singletonMap("pickId", pickId)) //
        .send().join();

    System.out.println("Correlated " + message );
  }

  @Transactional
  public void goodsShippedReceived(Message<GoodsShippedEventPayload> message) {
    String shipmentId = message.getData().getShipmentId();     

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) //
        .variables(Collections.singletonMap("shipmentId", shipmentId)) //
        .send().join();

    System.out.println("Correlated " + message );
  }
}

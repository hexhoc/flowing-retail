package io.flowing.retail.order.messages;

import java.io.IOException;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.flowing.retail.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.camunda.zeebe.client.ZeebeClient;
import io.flowing.retail.order.process.OrderFlowContext;
import io.flowing.retail.order.process.payload.GoodsFetchedEventPayload;
import io.flowing.retail.order.process.payload.GoodsShippedEventPayload;
import io.flowing.retail.order.process.payload.PaymentReceivedEventPayload;
import io.flowing.retail.order.persistence.OrderRepository;

@Component
@RequiredArgsConstructor
public class MessageListener {

  private final OrderRepository repository;

  private final ZeebeClient zeebe;
	
  private final ObjectMapper objectMapper;

  @KafkaListener(id = "order", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{

    if ("PaymentReceivedEvent".equals(messageType)) {
      paymentReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<Message<PaymentReceivedEventPayload>>() {}));
    }
    else if ("GoodsFetchedEvent".equals(messageType)) {
      goodsFetchedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<Message<GoodsFetchedEventPayload>>() {}));
    }
    else if ("GoodsShippedEvent".equals(messageType)) {
      goodsShippedReceived(objectMapper.readValue(messagePayloadJson, new TypeReference<Message<GoodsShippedEventPayload>>() {}));
    }
    else {
      System.out.println("Ignored message of type " + messageType );
    }
  }

  @Transactional
  public void paymentReceived(Message<PaymentReceivedEventPayload> message) throws Exception {
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
  public void goodsFetchedReceived(Message<GoodsFetchedEventPayload> message) throws Exception {
    String pickId = message.getData().getPickId();     

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) // 
        .variables(Collections.singletonMap("pickId", pickId)) //
        .send().join();

    System.out.println("Correlated " + message );
  }


  @Transactional
  public void goodsShippedReceived(Message<GoodsShippedEventPayload> message) throws Exception {
    String shipmentId = message.getData().getShipmentId();     

    zeebe.newPublishMessageCommand() //
        .messageName(message.getType()) //
        .correlationKey(message.getCorrelationid()) //
        .variables(Collections.singletonMap("shipmentId", shipmentId)) //
        .send().join();

    System.out.println("Correlated " + message );
  }
}

package io.flowing.retail.payment.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MessageListener {    
  
  private final MessageSender messageSender;

  private final ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(id = "payment", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if (!"RetrievePaymentCommand".equals(messageType)) {
      System.out.println("Ignoring message of type " + messageType);
      return;
    }

    Message<RetrievePaymentCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<RetrievePaymentCommandPayload>>(){});
    RetrievePaymentCommandPayload retrievePaymentCommand = message.getData();
    
    System.out.println("Retrieve payment: " + retrievePaymentCommand.getAmount() + " for " + retrievePaymentCommand.getRefId());

    Thread.sleep(20_000);

    messageSender.send( //
        new Message<PaymentReceivedEventPayload>( //
            "PaymentReceivedEvent", //
            message.getTraceid(), //
            new PaymentReceivedEventPayload() //
              .setRefId(retrievePaymentCommand.getRefId()))
        .setCorrelationid(message.getCorrelationid()));

  }
  
    
    
}

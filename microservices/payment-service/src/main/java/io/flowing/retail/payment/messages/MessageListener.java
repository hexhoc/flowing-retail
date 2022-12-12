package io.flowing.retail.payment.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.payment.messages.payload.PaymentReceivedEventPayload;
import io.flowing.retail.payment.messages.payload.RetrievePaymentCommandPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log
public class MessageListener {    
  
  private final MessageSender messageSender;
  private final ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(id = "payment", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{
    if (!"RetrievePaymentCommand".equals(messageType)) {
      log.info("Ignoring message of type " + messageType);
      return;
    }

    Message<RetrievePaymentCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<>(){});
    RetrievePaymentCommandPayload retrievePaymentCommand = message.getData();
    
    log.info("Retrieve payment: " + retrievePaymentCommand.getAmount() + " for " + retrievePaymentCommand.getRefId());

    // Processing. Long operation
    // TODO Add PaymentService class for todo processing
    Thread.sleep(60_000);

    messageSender.send( //
        new Message<>( //
            "PaymentReceivedEvent", //
            message.getTraceid(), //
            new PaymentReceivedEventPayload(retrievePaymentCommand.getRefId()))
        .setCorrelationid(message.getCorrelationid()));
  }
}

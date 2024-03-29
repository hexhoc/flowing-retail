package io.flowing.retail.paymentservice.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.paymentservice.messages.payload.PaymentReceivedEventPayload;
import io.flowing.retail.paymentservice.messages.payload.RetrievePaymentCommandPayload;
import io.flowing.retail.paymentservice.service.PaymentService;
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
  
  private final PaymentService paymentService;
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
    String paymentId = paymentService.receive(retrievePaymentCommand);
    
    messageSender.send( //
        new Message<>( //
            "PaymentReceivedEvent", //
            message.getTraceid(), //
            new PaymentReceivedEventPayload(retrievePaymentCommand.getRefId(), paymentId))
        .setCorrelationid(message.getCorrelationid()));
  }
}

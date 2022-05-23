package io.flowing.retail.kafka.order.process;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.flowing.retail.kafka.order.domain.Order;
import io.flowing.retail.kafka.order.process.payload.RetrievePaymentCommandPayload;
import io.flowing.retail.kafka.order.messages.Message;
import io.flowing.retail.kafka.order.messages.MessageSender;
import io.flowing.retail.kafka.order.persistence.OrderRepository;
import io.camunda.zeebe.client.api.response.ActivatedJob;

@Component
@RequiredArgsConstructor
public class RetrievePaymentAdapter {

  private final MessageSender messageSender;

  private final OrderRepository orderRepository;

  @ZeebeWorker(type = "retrieve-payment", autoComplete = true)
  public Map<String, String> handle(ActivatedJob job) {
    OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
    
    Order order = orderRepository.findById(context.getOrderId()).get();   
            
    // generate an UUID for this communication
    String correlationId = UUID.randomUUID().toString();

    messageSender.send( //
        new Message<RetrievePaymentCommandPayload>( //
            "RetrievePaymentCommand", //
            context.getTraceId(), //
            new RetrievePaymentCommandPayload() //
              .setRefId(order.getId()) //
              .setReason("order") //
              .setAmount(order.getTotalSum())) //
        .setCorrelationid(correlationId));
    
    return Collections.singletonMap("CorrelationId_RetrievePayment", correlationId);
  }

}

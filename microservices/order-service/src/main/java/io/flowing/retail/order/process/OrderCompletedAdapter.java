package io.flowing.retail.order.process;

import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.flowing.retail.order.process.payload.OrderCompletedEventPayload;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.camunda.zeebe.client.api.response.ActivatedJob;

@Component
public class OrderCompletedAdapter {
  
  @Autowired
  private MessageSender messageSender;  

  @ZeebeWorker(type = "order-completed", autoComplete = true)
  public void handle(ActivatedJob job) {
    OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
       
    messageSender.send( //
        new Message<OrderCompletedEventPayload>( //
            "OrderCompletedEvent", //
            context.getTraceId(), //
            new OrderCompletedEventPayload() //
              .setOrderId(context.getOrderId())));
    
    //TODO: Reintorduce traceId?     .setCorrelationId(event.get)));
  }

  

}

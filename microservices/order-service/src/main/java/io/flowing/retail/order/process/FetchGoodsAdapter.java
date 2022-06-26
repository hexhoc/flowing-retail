package io.flowing.retail.order.process;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.flowing.retail.order.repository.OrderRepository;
import io.flowing.retail.order.process.payload.FetchGoodsCommandPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;

@Component
@RequiredArgsConstructor
public class FetchGoodsAdapter {
  
  private final MessageSender messageSender;
  
  private final OrderRepository orderRepository;

  @ZeebeWorker(type = "fetch-goods", autoComplete = true)
  public Map<String, String> handle(ActivatedJob job) {
    OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
    Order order = orderRepository.findById( context.getOrderId() ).get();
    
    // generate an UUID for this communication
    String correlationId = UUID.randomUUID().toString();
        
    messageSender.send(new Message<FetchGoodsCommandPayload>( //
            "FetchGoodsCommand", //
            context.getTraceId(), //
            new FetchGoodsCommandPayload() //
              .setRefId(order.getId()) //
              .setItems(order.getItems())) //
        .setCorrelationid(correlationId));
    
    return Collections.singletonMap("CorrelationId_FetchGoods", correlationId);
  }
  
}

package io.flowing.retail.order.process;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.flowing.retail.order.process.payload.FetchGoodsCommandPayload;
import io.flowing.retail.order.process.payload.OrderCompletedEventPayload;
import io.flowing.retail.order.process.payload.RetrievePaymentCommandPayload;
import io.flowing.retail.order.process.payload.ShipGoodsCommandPayload;
import io.flowing.retail.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Log4j2
public class OrderKafkaProcess {

    private final MessageSender messageSender;

    private final OrderRepository orderRepository;

    private final ZeebeClient zeebeClient;

    public void startProcess(String orderId) {
        // This context is camunda variable, that flow from one task to another
        OrderFlowContext context = new OrderFlowContext();
        context.setOrderId(orderId);
        context.setTraceId(UUID.randomUUID().toString());

        log.info(String.format("New order placed, start flow with %s", context));
        // and kick off a new flow instance
        zeebeClient.newCreateInstanceCommand() //
                .bpmnProcessId("order-kafka") //
                .latestVersion() //
                .variables(context.asMap()) //
                .send().join();
    }

    @ZeebeWorker(type = "retrieve-payment", autoComplete = true)
    public Map<String, String> retrievePaymentHandle(ActivatedJob job) {
        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());

        Order order = orderRepository.findById(UUID.fromString(context.getOrderId())).get();

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

    @ZeebeWorker(type = "fetch-goods", autoComplete = true)
    public Map<String, String> fetchGoodsHandle(ActivatedJob job) {
        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
        Order order = orderRepository.findById(UUID.fromString(context.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Order not found"));

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

    @ZeebeWorker(type = "ship-goods", autoComplete = true)
    public Map<String, String> shipGoodsHandle(ActivatedJob job) {
        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
        Order order = orderRepository.findById(UUID.fromString(context.getOrderId())).get();

        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        messageSender.send(new Message<ShipGoodsCommandPayload>( //
                "ShipGoodsCommand", //
                context.getTraceId(), //
                new ShipGoodsCommandPayload() //
                        .setRefId(order.getId())
                        .setPickId(context.getPickId()) //
                        .setRecipientName(order.getCustomer().getName()) //
                        .setRecipientAddress(order.getCustomer().getAddress())) //
                .setCorrelationid(correlationId));

        return Collections.singletonMap("CorrelationId_ShipGoods", correlationId);
    }

    @ZeebeWorker(type = "order-completed", autoComplete = true)
    public void orderCompletedHandle(ActivatedJob job) {
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

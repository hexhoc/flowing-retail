package io.flowing.retail.order.process;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import io.flowing.retail.order.dto.CustomerDto;
import io.flowing.retail.order.dto.OrderDto;
import io.flowing.retail.order.entity.Order;
import io.flowing.retail.order.entity.OrderStatusEnum;
import io.flowing.retail.order.messages.Message;
import io.flowing.retail.order.messages.MessageSender;
import io.flowing.retail.order.process.payload.FetchGoodsCommandPayload;
import io.flowing.retail.order.process.payload.OrderCompletedEventPayload;
import io.flowing.retail.order.process.payload.RetrievePaymentCommandPayload;
import io.flowing.retail.order.process.payload.ShipGoodsCommandPayload;
import io.flowing.retail.order.repository.OrderRepository;
import io.flowing.retail.order.service.CustomerService;
import io.flowing.retail.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Log
public class OrderKafkaProcess {

    private final MessageSender messageSender;
    private final OrderService orderService;
    private final ZeebeClient zeebeClient;

    /**
     * Starting an order processing business process
     */
    public void startProcess(String orderId) {
        // This context is camunda variable, that flow from one task to another
        // TODO: Рассмотреть вариант, вместо id заказа лучше подставлять dto заказа
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

    /**
     * Step 1. Send message about order to payment-service through kafka
     * To receive payment confirmation
     * 
     * @return retrieve payment correlation id (camunda check message by this
     *         correlation id)
     */
    @ZeebeWorker(type = "retrieve-payment", autoComplete = true)
    public Map<String, String> retrievePaymentHandle(ActivatedJob job) {
        log.info("retrieve-payment job");
        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());

        OrderDto orderDto = orderService.findById(context.getOrderId());
        orderService.updateStatus(orderDto.getOrderId(), OrderStatusEnum.PAYMENT_AWAITING);
        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        messageSender.send( //
                new Message<>( //
                        "RetrievePaymentCommand", //
                        context.getTraceId(), //
                        new RetrievePaymentCommandPayload() //
                                .setRefId(orderDto.getOrderId()) //
                                .setReason("order") //
                                .setAmount(orderDto.getTotalSum())) //
                        .setCorrelationid(correlationId));

        return Collections.singletonMap("CorrelationId_RetrievePayment", correlationId);
    }

    /**
     * Step 2. Send message to inventory-service through kafka
     * To check the balance of goods in stock
     * 
     * @return retrieve FetchGoods correlation id (camunda check message by this
     *         correlation id)
     */
    @ZeebeWorker(type = "fetch-goods", autoComplete = true)
    public Map<String, String> fetchGoodsHandle(ActivatedJob job) {
        log.info("fetch-goods job");

        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
        OrderDto orderDto = orderService.findById(context.getOrderId());
        orderService.updateStatus(orderDto.getOrderId(), OrderStatusEnum.ALLOCATION_AWAITING);

        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        messageSender.send(new Message<>( //
                "FetchGoodsCommand", //
                context.getTraceId(), //
                new FetchGoodsCommandPayload() //
                        .setRefId(orderDto.getOrderId()) //
                        .setItems(orderDto.getItems())) //
                .setCorrelationid(correlationId));

        return Collections.singletonMap("CorrelationId_FetchGoods", correlationId);
    }

    /**
     * Step 3. Send message to shipping-service through kafka
     * To prepare the goods for shipment to the buyer
     * 
     * @return retrieve CorrelationId_ShipGoods (camunda check message by this
     *         correlation id)
     */
    @ZeebeWorker(type = "ship-goods", autoComplete = true)
    public Map<String, String> shipGoodsHandle(ActivatedJob job) {
        log.info("ship-goods job");

        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());
        OrderDto orderDto = orderService.findById(context.getOrderId());
        CustomerDto customerDto = orderDto.getCustomer();
        orderService.updateStatus(orderDto.getOrderId(), OrderStatusEnum.DELIVERY_AWAITING);

        // generate an UUID for this communication
        String correlationId = UUID.randomUUID().toString();

        messageSender.send(new Message<>( //
                "ShipGoodsCommand", //
                context.getTraceId(), //
                new ShipGoodsCommandPayload() //
                        .setRefId(orderDto.getOrderId())
                        .setPickId(context.getPickId()) //
                        .setRecipientName(customerDto.getName()) //
                        .setRecipientAddress(customerDto.getAddress())) //
                .setCorrelationid(correlationId));

        return Collections.singletonMap("CorrelationId_ShipGoods", correlationId);
    }

    @ZeebeWorker(type = "order-completed", autoComplete = true)
    public void orderCompletedHandle(ActivatedJob job) {
        log.info("order-completed job");

        OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());

        messageSender.send( //
                new Message<OrderCompletedEventPayload>( //
                        "OrderCompletedEvent", //
                        context.getTraceId(), //
                        new OrderCompletedEventPayload() //
                                .setOrderId(context.getOrderId())));

        // TODO: Reintorduce traceId? .setCorrelationId(event.get)));
    }
}

package io.flowingretail.orderservice.adapter.in.kafka.handler;

import static io.flowingretail.common.constants.EventTypeConstants.FETCH_GOODS_COMMAND;
import static io.flowingretail.common.constants.ServiceNameConstants.ORDER_SERVICE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.orderservice.adapter.in.kafka.command.FetchGoodsCommandPayload;
import io.flowingretail.orderservice.adapter.in.kafka.event.PaymentReceivedEvent;
import io.flowingretail.orderservice.adapter.in.kafka.event.PaymentReceivedEventPayload;
import io.flowingretail.orderservice.adapter.out.kafka.MessageSender;
import io.flowingretail.orderservice.dto.InventoryItemDto;
import io.flowingretail.orderservice.dto.OrderDto;
import io.flowingretail.orderservice.service.OrderService;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentReceivedEventHandler {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final IncomingEventService incomingEventService;
    private final MessageSender messageSender;

    @EventListener
    @Transactional
    public void on(PaymentReceivedEvent event) throws JsonProcessingException {
        log.info("PaymentReceivedEvent");
        Message<PaymentReceivedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))){
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        // TODO: Check. Payment success or not
        PaymentReceivedEventPayload paymentReceivedEventPayload = message.getData();
        orderService.changeStatus(paymentReceivedEventPayload.getRefId());
        var orderDto = orderService.findById(paymentReceivedEventPayload.getRefId());

        sendResponse(message, orderDto);

        log.info("Payment id: " + paymentReceivedEventPayload.getPaymentId());
        log.info("Correlated " + message);
    }

    private void sendResponse(Message<?> message, OrderDto orderDto) {
        var fetchGoodsCommandPayload = new FetchGoodsCommandPayload()
            .setRefId(orderDto.getId())
            .setItems(orderDto.getOrderItems().stream()
                .map(i -> new InventoryItemDto(i.getProductId(), i.getQuantity()))
                .collect(Collectors.toSet()));

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(FETCH_GOODS_COMMAND)
            .data(fetchGoodsCommandPayload)
            .source(ORDER_SERVICE)
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(UUID.randomUUID().toString())
            .build();

        messageSender.send(responseMessage, KafkaConfig.INVENTORY_TOPIC);
    }

}
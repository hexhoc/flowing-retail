package io.flowingretail.orderservice.adapter.in.kafka.handler;

import static io.flowingretail.common.constants.EventTypeConstants.SHIP_GOODS_COMMAND;
import static io.flowingretail.common.constants.ServiceNameConstants.ORDER_SERVICE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.orderservice.adapter.in.kafka.command.ShipGoodsCommandPayload;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsFetchedEvent;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsFetchedEventPayload;
import io.flowingretail.orderservice.adapter.out.http.CustomerRestClient;
import io.flowingretail.orderservice.adapter.out.kafka.MessageSender;
import io.flowingretail.orderservice.dto.CustomerDto;
import io.flowingretail.orderservice.dto.OrderDto;
import io.flowingretail.orderservice.service.OrderService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoodsFetchedEventHandler {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final IncomingEventService incomingEventService;
    private final MessageSender messageSender;
    private final CustomerRestClient customerRestClient;

    @EventListener
    @Transactional
    public void on(GoodsFetchedEvent event) throws JsonProcessingException {
        log.info("GoodsFetchedEvent");
        Message<GoodsFetchedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))){
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        // TODO: Check. Fetched success or not
        GoodsFetchedEventPayload goodsFetchedEventPayload = message.getData();
        orderService.changeStatus(goodsFetchedEventPayload.getRefId());

        OrderDto orderDto = orderService.findById(goodsFetchedEventPayload.getRefId());
        CustomerDto customerDto = customerRestClient.getCustomerById(orderDto.getCustomerId());

        sendResponse(message, orderDto, customerDto);

        log.info("pick id: " + goodsFetchedEventPayload.getPickId());
        log.info("Correlated " + message);
    }

    private void sendResponse(Message<?> message, OrderDto orderDto, CustomerDto customerDto) {
        var shipGoodsCommandPayload = new ShipGoodsCommandPayload()
            .setRefId(orderDto.getId())
            .setRecipientName(String.format("%s %s", customerDto.getFirstName(), customerDto.getLastName()))
            .setRecipientAddress(orderDto.getAddress())
            .setLogisticsProvider("DHL");

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(SHIP_GOODS_COMMAND)
            .data(shipGoodsCommandPayload)
            .source(ORDER_SERVICE)
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(UUID.randomUUID().toString())
            .build();

        messageSender.send(responseMessage, KafkaConfig.SHIPMENT_TOPIC);
    }
}

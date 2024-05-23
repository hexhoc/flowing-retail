package io.flowingretail.shippingservice.adapter.in.kafka.handler;

import static io.flowingretail.common.constants.EventTypeConstants.GOODS_SHIPPED_EVENT;
import static io.flowingretail.common.constants.ServiceNameConstants.SHIPPING_SERVICE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.shippingservice.adapter.in.kafka.command.ShipGoodsCommand;
import io.flowingretail.shippingservice.adapter.in.kafka.command.ShipGoodsCommandPayload;
import io.flowingretail.shippingservice.adapter.in.kafka.event.GoodsShippedEventPayload;
import io.flowingretail.shippingservice.adapter.out.kafka.MessageSender;
import io.flowingretail.shippingservice.service.ShippingService;
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
public class ShipGoodCommandHandler {
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final ShippingService shippingService;
    private final IncomingEventService incomingEventService;

    @EventListener
    @Transactional
    public void on(ShipGoodsCommand event) throws JsonProcessingException, InterruptedException {
        log.info("3/3 ShipGoodsCommand");

        Message<ShipGoodsCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))){
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        String shipmentId = shippingService.createShipment(
            message.getData().getRefId(),
            message.getData().getRecipientName(),
            message.getData().getRecipientAddress(),
            message.getData().getLogisticsProvider());

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(GOODS_SHIPPED_EVENT)
            .data(new GoodsShippedEventPayload(message.getData().getRefId(), shipmentId))
            .source(SHIPPING_SERVICE)
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(UUID.randomUUID().toString())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

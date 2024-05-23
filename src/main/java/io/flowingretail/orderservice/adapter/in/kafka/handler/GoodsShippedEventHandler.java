package io.flowingretail.orderservice.adapter.in.kafka.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsShippedEvent;
import io.flowingretail.orderservice.adapter.in.kafka.event.GoodsShippedEventPayload;
import io.flowingretail.orderservice.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoodsShippedEventHandler {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final IncomingEventService incomingEventService;

    @EventListener
    @Transactional
    public void on(GoodsShippedEvent event) throws JsonProcessingException {
        log.info("GoodsShippedEvent");
        Message<GoodsShippedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))){
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        // TODO: Check. Shipment success or not
        GoodsShippedEventPayload goodsShippedEventPayload = message.getData();
        orderService.changeStatus(goodsShippedEventPayload.getRefId());

        log.info("shipment id: " + goodsShippedEventPayload.getShipmentId());
        log.info("Correlated " + message);
    }
}

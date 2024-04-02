package io.flowingretail.orderservice.messages.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.event.GoodsShippedEvent;
import io.flowingretail.common.messages.event.GoodsShippedEventPayload;
import io.flowingretail.orderservice.service.OrderService;
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

    @EventListener
    @Transactional
    public void on(GoodsShippedEvent event) throws JsonProcessingException {
        log.info("GoodsShippedEvent");
        Message<GoodsShippedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});

        // TODO: Check. Shipment success or not
        GoodsShippedEventPayload goodsShippedEventPayload = message.getData();
        orderService.changeStatus(goodsShippedEventPayload.getRefId());

        log.info("shipment id: " + goodsShippedEventPayload.getShipmentId());
        log.info("Correlated " + message);
    }
}

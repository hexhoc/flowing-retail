package io.flowingretail.orderservice.messages.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.event.GoodsFetchedEvent;
import io.flowingretail.common.messages.event.GoodsFetchedEventPayload;
import io.flowingretail.orderservice.service.OrderService;
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

    @EventListener
    @Transactional
    public void on(GoodsFetchedEvent event) throws JsonProcessingException {
        log.info("GoodsFetchedEvent");
        Message<GoodsFetchedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});

        // TODO: Check. Fetched success or not
        GoodsFetchedEventPayload goodsFetchedEventPayload = message.getData();
        orderService.changeStatus(goodsFetchedEventPayload.getRefId());

        log.info("pick id: " + goodsFetchedEventPayload.getPickId());
        log.info("Correlated " + message);
    }
}

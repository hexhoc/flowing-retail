package io.flowingretail.inventoryservice.messages;

import static io.flowingretail.common.constants.EventTypeConstants.GOODS_FETCHED_EVENT;
import static io.flowingretail.common.constants.ServiceNameConstants.INVENTORY_SERVICE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.FetchGoodsCommand;
import io.flowingretail.common.messages.command.FetchGoodsCommandPayload;
import io.flowingretail.common.messages.event.GoodsFetchedEventPayload;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.inventoryservice.service.ProductStockCommandService;
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
public class FetchGoodsCommandHandler {
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final ProductStockCommandService productStockCommandService;
    private final IncomingEventService incomingEventService;

    @EventListener
    @Transactional
    public void on(FetchGoodsCommand event) throws JsonProcessingException {
        log.info("GoodsFetchedEvent");
        Message<FetchGoodsCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))) {
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
        productStockCommandService.pickItems( //
            fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(GOODS_FETCHED_EVENT)
            .data(new GoodsFetchedEventPayload(fetchGoodsCommand.getRefId(), fetchGoodsCommand.getRefId()))
            .source(INVENTORY_SERVICE)
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(UUID.randomUUID().toString())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

package io.flowingretail.inventoryservice.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.FetchGoodsCommand;
import io.flowingretail.common.messages.command.FetchGoodsCommandPayload;
import io.flowingretail.common.messages.event.GoodsFetchedEventPayload;
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

    @EventListener
    @Transactional
    public void on(FetchGoodsCommand event) throws JsonProcessingException {
        log.info("GoodsFetchedEvent");
        Message<FetchGoodsCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {
        });

        FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
        productStockCommandService.pickItems( //
            fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type("GoodsFetchedEvent")
            .data(new GoodsFetchedEventPayload(fetchGoodsCommand.getRefId(), fetchGoodsCommand.getRefId()))
            .source("inventory-service")
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(message.getCorrelationid())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

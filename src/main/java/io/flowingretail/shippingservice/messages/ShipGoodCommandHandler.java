package io.flowingretail.shippingservice.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.RetrievePaymentCommand;
import io.flowingretail.common.messages.command.RetrievePaymentCommandPayload;
import io.flowingretail.common.messages.command.ShipGoodsCommand;
import io.flowingretail.common.messages.command.ShipGoodsCommandPayload;
import io.flowingretail.common.messages.event.GoodsShippedEventPayload;
import io.flowingretail.common.messages.event.PaymentReceivedEventPayload;
import io.flowingretail.paymentservice.service.PaymentService;
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

    @EventListener
    @Transactional
    public void on(ShipGoodsCommand event) throws JsonProcessingException, InterruptedException {
        log.info("ShipGoodsCommand");

        Message<ShipGoodsCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {
        });

        String shipmentId = shippingService.createShipment(
            message.getData().getRefId(),
            message.getData().getRecipientName(),
            message.getData().getRecipientAddress(),
            message.getData().getLogisticsProvider());

        Thread.sleep(5_000);

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type("GoodsShippedEvent")
            .data(new GoodsShippedEventPayload(message.getData().getRefId(), shipmentId))
            .source("shipping-service")
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(message.getCorrelationid())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

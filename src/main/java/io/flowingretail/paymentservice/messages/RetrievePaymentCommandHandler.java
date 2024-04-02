package io.flowingretail.paymentservice.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.command.RetrievePaymentCommand;
import io.flowingretail.common.messages.command.RetrievePaymentCommandPayload;
import io.flowingretail.common.messages.event.PaymentReceivedEventPayload;
import io.flowingretail.paymentservice.service.PaymentService;
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
public class RetrievePaymentCommandHandler {
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @EventListener
    @Transactional
    public void on(RetrievePaymentCommand event) throws JsonProcessingException, InterruptedException {
        log.info("RetrievePaymentCommand");

        Message<RetrievePaymentCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>(){});
        RetrievePaymentCommandPayload retrievePaymentCommand = message.getData();

        log.info("Retrieve payment: " + retrievePaymentCommand.getAmount() + " for " + retrievePaymentCommand.getRefId());

        // Processing. Long operation
        String paymentId = paymentService.receive(retrievePaymentCommand);

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type("PaymentReceivedEvent")
            .data(new PaymentReceivedEventPayload(retrievePaymentCommand.getRefId(), paymentId))
            .source("payment-service")
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(message.getCorrelationid())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

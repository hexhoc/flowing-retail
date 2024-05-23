package io.flowingretail.paymentservice.adapter.in.kafka.handler;

import static io.flowingretail.common.constants.EventTypeConstants.PAYMENT_RECEIVED_EVENT;
import static io.flowingretail.common.constants.ServiceNameConstants.PAYMENT_SERVICE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.config.KafkaConfig;
import io.flowingretail.common.service.IncomingEventService;
import io.flowingretail.paymentservice.adapter.in.kafka.command.RetrievePaymentCommand;
import io.flowingretail.paymentservice.adapter.in.kafka.command.RetrievePaymentCommandPayload;
import io.flowingretail.paymentservice.adapter.in.kafka.event.PaymentReceivedEventPayload;
import io.flowingretail.paymentservice.adapter.out.kafka.MessageSender;
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
    private final IncomingEventService incomingEventService;

    @EventListener
    @Transactional
    public void on(RetrievePaymentCommand event) throws JsonProcessingException, InterruptedException {
        log.info("1/3 RetrievePaymentCommand");

        Message<RetrievePaymentCommandPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>(){});
        if (incomingEventService.alreadyExist(UUID.fromString(message.getCorrelationid()))){
            log.warn("Message with trace id %s already exist".formatted(message.getTraceid()));
            return;
        }

        incomingEventService.createEvent(message, event.getPayload());

        RetrievePaymentCommandPayload retrievePaymentCommand = message.getData();

        log.info("Retrieve payment: " + retrievePaymentCommand.getAmount() + " for " + retrievePaymentCommand.getRefId());

        // Processing. Long operation
        String paymentId = paymentService.receive(retrievePaymentCommand);

        var responseMessage = Message.builder()
            .id(UUID.randomUUID().toString())
            .type(PAYMENT_RECEIVED_EVENT)
            .data(new PaymentReceivedEventPayload(retrievePaymentCommand.getRefId(), paymentId))
            .source(PAYMENT_SERVICE)
            .time(LocalDateTime.now())
            .traceid(message.getTraceid())
            .correlationid(UUID.randomUUID().toString())
            .build();

        messageSender.send(responseMessage, KafkaConfig.ORDER_TOPIC);
    }
}

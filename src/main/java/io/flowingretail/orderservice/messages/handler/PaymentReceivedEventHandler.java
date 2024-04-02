package io.flowingretail.orderservice.messages.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.messages.MessageSender;
import io.flowingretail.common.messages.event.PaymentReceivedEvent;
import io.flowingretail.common.messages.event.PaymentReceivedEventPayload;
import io.flowingretail.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentReceivedEventHandler {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @EventListener
    @Transactional
    public void on(PaymentReceivedEvent event) throws JsonProcessingException {
        log.info("PaymentReceivedEvent");
        Message<PaymentReceivedEventPayload> message = objectMapper.readValue(event.getPayload(), new TypeReference<>() {});

        // TODO: Check. Payment success or not
        PaymentReceivedEventPayload paymentReceivedEventPayload = message.getData();
        orderService.changeStatus(paymentReceivedEventPayload.getRefId());

        log.info("Payment id: " + paymentReceivedEventPayload.getPaymentId());
        log.info("Correlated " + message);
    }
}

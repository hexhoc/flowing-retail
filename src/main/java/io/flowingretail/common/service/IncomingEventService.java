package io.flowingretail.common.service;

import io.flowingretail.common.adapter.in.kafka.Message;
import io.flowingretail.common.adapter.out.jpa.IncomingEventEntity;
import io.flowingretail.common.adapter.out.jpa.IncomingEventRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomingEventService {
    private final IncomingEventRepository incomingEventRepository;

    public Boolean alreadyExist(UUID correlationId) {
        log.info("Trying to find an existing event");
        return incomingEventRepository.existsById(correlationId);
    }

    public void createEvent(Message<?> message, String request) {
        var incomingEvent = new IncomingEventEntity(
            null,
            UUID.fromString(message.getTraceid()),
            UUID.fromString(message.getCorrelationid()),
            message.getSource(),
            message.getType(),
            request,
            LocalDateTime.now());

        incomingEventRepository.save(incomingEvent);
    }
}

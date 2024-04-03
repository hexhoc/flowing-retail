package io.flowingretail.common.service;

import io.flowingretail.common.entity.IncomingEvent;
import io.flowingretail.common.messages.Message;
import io.flowingretail.common.repository.IncomingEventRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomingEventService {
    private final IncomingEventRepository incomingEventRepository;

    public Boolean alreadyExist(UUID correlationId) {
        return incomingEventRepository.findByCorrelationId(correlationId).isPresent();
    }

    public void createEvent(Message<?> message, String request) {
        var incomingEvent = new IncomingEvent(
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

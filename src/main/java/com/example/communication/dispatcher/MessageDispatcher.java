package com.example.communication.dispatcher;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageDispatcher {
    private final List<CommunicationService> providers;

    public MessageResponse dispatch(MessageRequest request) {
        log.info("Dispatching message for {} via {}", request.getTo(), request.getChannel());
        for (CommunicationService provider : providers) {
            if (!provider.supports(request)) {
                continue;
            }
            MessageResponse resp = provider.sendMessage(request);
            if (resp.isSuccess()) {
                log.info("Delivered by {}", provider.getClass().getSimpleName());
                return resp;
            }
            log.warn("{} failed: {}", provider.getClass().getSimpleName(), resp.getError());
        }
        log.error("All providers failed for {} to {}", request.getChannel(), request.getTo());
        return new MessageResponse(false, null, null, "All providers failed");
    }
}

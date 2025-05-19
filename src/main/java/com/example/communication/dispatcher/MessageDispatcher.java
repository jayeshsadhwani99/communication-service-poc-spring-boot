package com.example.communication.dispatcher;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageDispatcher {
    private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
    private final List<CommunicationService> providers;

    public MessageDispatcher(List<CommunicationService> providers) {
        this.providers = providers;
    }

    public MessageResponse dispatch(MessageRequest request) {
        for (CommunicationService provider : providers) {
            if (!provider.supports(request)) continue;
            MessageResponse resp = provider.sendMessage(request);
            if (resp.isSuccess()) {
                return resp;
            }
            log.warn("{} failed: {}", provider.getClass().getSimpleName(), resp.getError());
        }
        return new MessageResponse(false, null, null, "All providers failed");
    }
}
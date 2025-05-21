package com.example.communication.service.impl;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Order(1)
@Slf4j
public class MetaWhatsAppService implements CommunicationService {
    private final WebClient webClient;

    @Value("${meta.business.phone-number-id}")
    private String phoneNumberId;

    @Value("${meta.business.access-token}")
    private String token;

    public MetaWhatsAppService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://graph.facebook.com/v17.0")
                .build();
    }

    @Override
    public boolean supports(MessageRequest request) {
        boolean supported = request.getChannel() == MessageRequest.Channel.WHATSAPP
                && !request.getTo().startsWith("+91");
        log.debug("MetaWhatsAppService supports {}? {}", request.getTo(), supported);
        return supported;
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            Map<String, Object> payload = Map.of(
                    "messaging_product", "whatsapp",
                    "to", request.getTo(),
                    "type", "text",
                    "text", Map.of("body", request.getContent()));

            JsonNode resp = webClient.post()
                    .uri("/{id}/messages", phoneNumberId)
                    .headers(headers -> headers.setBearerAuth(token))
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            String msgId = resp.get("messages").get(0).get("id").asText();
            log.info("MetaWhatsApp: sent to {} (id={})", request.getTo(), msgId);
            return new MessageResponse(true, "META", msgId, null);

        } catch (Exception e) {
            log.error("MetaWhatsApp: send failed to {}", request.getTo(), e);
            return new MessageResponse(false, "META", null, e.getMessage());
        }
    }
}

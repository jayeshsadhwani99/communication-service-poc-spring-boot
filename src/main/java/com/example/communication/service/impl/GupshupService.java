package com.example.communication.service.impl;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Order(0)
@Slf4j
public class GupshupService implements CommunicationService {
    private final WebClient webClient;

    @Value("${gupshup.api-key}")
    private String apiKey;

    public GupshupService(WebClient.Builder builder,
            @Value("${gupshup.base-url}") String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public boolean supports(MessageRequest request) {
        return request.getChannel() == MessageRequest.Channel.WHATSAPP
                && request.getTo().startsWith("+91");
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            String form = String.format(
                    "channel=whatsapp&source=%s&destination=%s&message=%s",
                    "YOUR_SOURCE_ID",
                    request.getTo(),
                    URLEncoder.encode(request.getContent(), StandardCharsets.UTF_8));

            String resp = webClient.post()
                    .uri("/msg")
                    .header("apikey", apiKey)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(form)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Gupshup: message sent to {}", request.getTo());
            return new MessageResponse(true, "GUPSHUP", resp, null);

        } catch (Exception e) {
            log.error("Gupshup: send failed", e);
            return new MessageResponse(false, "GUPSHUP", null, e.getMessage());
        }
    }
}

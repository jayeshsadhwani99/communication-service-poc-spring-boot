package com.example.communication.template;

import com.example.communication.config.ZohoProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ZohoAuthService {

    private final WebClient zohoWebClient;
    private final ZohoProperties props;

    private String cachedToken;
    private AtomicLong expiry = new AtomicLong(0);

    public synchronized String getAccessToken() {
        long now = Instant.now().getEpochSecond();
        if (cachedToken != null && now < expiry.get() - 60) {
            return cachedToken;
        }
        JsonNode resp = zohoWebClient.post()
                .uri(uri -> uri
                        .path("/oauth/v2/token")
                        .queryParam("refresh_token", props.getRefreshToken())
                        .queryParam("client_id", props.getClientId())
                        .queryParam("client_secret", props.getClientSecret())
                        .queryParam("grant_type", "refresh_token")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        cachedToken = resp.path("access_token").asText();
        expiry.set(now + resp.path("expires_in").asInt());
        return cachedToken;
    }
}

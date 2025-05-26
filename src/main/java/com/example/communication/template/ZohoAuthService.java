package com.example.communication.template;

import com.example.communication.config.ZohoProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ZohoAuthService {

    private final ZohoProperties props;

    // Cached token + expiry
    private String cachedToken;
    private final AtomicLong expiryEpochSec = new AtomicLong(0);

    /**
     * Fetches a valid OAuth2 access token, refreshing via form‐encoded POST if
     * expired.
     */
    public synchronized String getAccessToken() {
        long now = Instant.now().getEpochSecond();
        if (cachedToken != null && now < expiryEpochSec.get() - 60) {
            return cachedToken;
        }

        // Build a WebClient for the accounts endpoint
        WebClient client = WebClient.builder()
                .baseUrl(props.getAuthBaseUrl())
                .build();

        // Prepare form parameters
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("refresh_token", props.getRefreshToken());
        form.add("client_id", props.getClientId());
        form.add("client_secret", props.getClientSecret());
        form.add("grant_type", "refresh_token");

        JsonNode resp = client.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // Extract and cache
        cachedToken = resp.path("access_token").asText();
        int expiresIn = resp.path("expires_in").asInt();
        expiryEpochSec.set(now + expiresIn);

        return cachedToken;
    }
}

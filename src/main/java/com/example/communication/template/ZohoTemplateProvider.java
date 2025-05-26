package com.example.communication.template;

import com.example.communication.config.ZohoProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Component // auto-detected
@RequiredArgsConstructor
public class ZohoTemplateProvider implements TemplateProvider {

    public static final String PROVIDER_ID = "zoho";

    private final WebClient zohoWebClient;
    private final ZohoAuthService authService;
    private final ZohoProperties props;

    // cache raw template bodies for 1h
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .maximumSize(100)
            .build();

    @Override
    public String getProviderId() {
        return PROVIDER_ID;
    }

    @Override
    public String renderTemplate(String templateName,
            Map<String, String> variables) {
        String body = cache.get(templateName, this::fetchFromZoho);
        StringSubstitutor sub = new StringSubstitutor(variables, "{{", "}}");
        return sub.replace(body);
    }

    private String fetchFromZoho(String templateName) {
        String token = authService.getAccessToken();
        // example endpoint; adjust path as Zoho API requires
        JsonNode resp = zohoWebClient.get()
                .uri("/Settings/Templates/{name}", templateName)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // adjust JSON path per Zoho response
        return resp.path("data").get(0).path("content").asText();
    }
}

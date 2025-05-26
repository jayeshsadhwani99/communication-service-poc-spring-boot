package com.example.communication.template;

import com.example.communication.config.ZohoProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ZohoTemplateProvider implements TemplateProvider {

    public static final String PROVIDER_ID = "zoho";

    private final ZohoAuthService authService;
    private final ZohoProperties props;
    private final @Qualifier("zohoWebClient") WebClient zohoWebClient;

    private final Cache<String, String> templateCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1))
            .maximumSize(500)
            .build();

    @Override
    public String getProviderId() {
        return PROVIDER_ID;
    }

    @Override
    public String renderTemplate(String templateName,
            Map<String, String> variables) {
        String body = templateCache.get(templateName, this::fetchFromZoho);
        return new StringSubstitutor(variables, "{{", "}}").replace(body);
    }

    private String fetchFromZoho(String templateName) {
        String token = authService.getAccessToken();
        JsonNode resp = zohoWebClient.get()
                .uri("/Settings/Templates/{name}", templateName)
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        JsonNode data = resp.path("data");
        if (data.isArray() && data.size() > 0) {
            return data.get(0).path("content").asText();
        }
        throw new IllegalArgumentException("Zoho template not found: " + templateName);
    }
}

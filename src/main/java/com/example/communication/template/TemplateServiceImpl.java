package com.example.communication.template;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final List<TemplateProvider> providers;

    @Override
    public String render(String providerId,
            String templateName,
            Map<String, String> variables) {
        return providers.stream()
                .filter(p -> p.getProviderId().equalsIgnoreCase(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No template provider: " + providerId))
                .renderTemplate(templateName, variables);
    }
}

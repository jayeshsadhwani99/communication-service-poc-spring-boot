package com.example.communication.template;

import java.util.Map;

/**
 * SPI for a single template provider.
 */
public interface TemplateProvider {

    /**
     * Unique ID for this provider (e.g. "zoho", "local").
     */
    String getProviderId();

    /**
     * Fetch and render a template.
     *
     * @param templateName the provider-specific template identifier
     * @param variables    map of placeholder → value
     * @return rendered text
     */
    String renderTemplate(String templateName, Map<String, String> variables);
}

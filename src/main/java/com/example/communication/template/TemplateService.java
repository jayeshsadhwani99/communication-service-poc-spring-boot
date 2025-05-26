package com.example.communication.template;

import java.util.Map;

/**
 * Facade for all registered TemplateProvider implementations.
 */
public interface TemplateService {

    /**
     * Render a template via the specified provider.
     *
     * @param providerId   e.g. "zoho"
     * @param templateName provider's template id
     * @param variables    values to substitute
     */
    String render(String providerId,
            String templateName,
            Map<String, String> variables);
}

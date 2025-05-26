package com.example.communication.config;

import com.example.communication.filter.NotificationIdFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    // ─── 1. Notification ID filter registration ────────────────────────

    @Bean
    public FilterRegistrationBean<NotificationIdFilter> notificationFilter() {
        FilterRegistrationBean<NotificationIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new NotificationIdFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1); // run early in the filter chain
        return bean;
    }

    // ─── 2. Expose WebClient.Builder ───────────────────────────────────

    /**
     * Expose the Spring Boot–auto-configured WebClient.Builder
     * so you can customize or build multiple WebClients.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // ─── 3. Generic WebClient ──────────────────────────────────────────

    /**
     * A generic WebClient for ad-hoc HTTP calls anywhere in your app.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    // ─── 4. Zoho-specific WebClient ────────────────────────────────────

    /**
     * A WebClient preconfigured with Zoho's base URL.
     * Inject into your ZohoTemplateProvider like:
     * @Qualifier("zohoWebClient") WebClient zohoWebClient
     */
    @Bean
    @Qualifier("zohoWebClient")
    public WebClient zohoWebClient(
            WebClient.Builder builder,
            ZohoProperties props) {
        return builder
                .baseUrl(props.getApiBaseUrl())
                .build();
    }
}

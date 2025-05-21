package com.example.communication.config;

import com.example.communication.filter.NotificationIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicitly registers the NotificationIdFilter on /api/* URLs.
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<NotificationIdFilter> notificationFilter() {
        FilterRegistrationBean<NotificationIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new NotificationIdFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1); // Executes early in the chain
        return bean;
    }
}

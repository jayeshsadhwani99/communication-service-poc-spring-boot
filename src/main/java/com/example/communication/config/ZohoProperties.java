package com.example.communication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zoho")
public class ZohoProperties {
    private String apiBaseUrl;
    private String clientId;
    private String clientSecret;
    private String refreshToken;
}

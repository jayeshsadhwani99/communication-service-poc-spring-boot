package com.example.communication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "zoho")
public class ZohoProperties {

    /**
     * CRM API base URL, e.g. "https://www.zohoapis.com/crm/v2"
     */
    private String apiBaseUrl;

    /**
     * OAuth2 token endpoint base URL, e.g. "https://accounts.zoho.com/oauth/v2"
     */
    private String authBaseUrl;

    /** OAuth2 client ID */
    private String clientId;

    /** OAuth2 client secret */
    private String clientSecret;

    /** OAuth2 refresh token */
    private String refreshToken;
}

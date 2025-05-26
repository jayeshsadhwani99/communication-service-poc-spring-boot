package com.example.communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotBlank
    private String to;

    /** Either raw content… */
    private String content;

    /** …or templating via a provider */
    private String templateProvider; // e.g. "zoho"
    private String templateName;
    private Map<String, String> templateVariables;

    @NotNull
    @Builder.Default
    private Channel channel = Channel.WHATSAPP;

    public enum Channel {
        WHATSAPP, SMS, EMAIL
    }
}

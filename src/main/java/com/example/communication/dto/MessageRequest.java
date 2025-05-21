package com.example.communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotBlank
    private String to;

    @NotBlank
    private String content;

    @NotNull
    private Channel channel = Channel.WHATSAPP;

    public enum Channel {
        WHATSAPP, SMS
    }
}

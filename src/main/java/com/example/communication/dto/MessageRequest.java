package com.example.communication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {

    @NotBlank
    private String to;

    @NotBlank
    private String content;

    @NotNull
    private Channel channel = Channel.WHATSAPP;

    public enum Channel { WHATSAPP, SMS }

    public MessageRequest() {}

    public MessageRequest(String to, String content, Channel channel) {
        this.to = to;
        this.content = content;
        this.channel = channel;
    }

    // getters & setters
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Channel getChannel() { return channel; }
    public void setChannel(Channel channel) { this.channel = channel; }
}
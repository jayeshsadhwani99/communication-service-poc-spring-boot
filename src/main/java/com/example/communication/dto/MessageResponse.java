package com.example.communication.dto;

public class MessageResponse {
    private boolean success;
    private String provider;
    private String messageId;
    private String error;

    public MessageResponse() {}
    public MessageResponse(boolean success, String provider, String messageId, String error) {
        this.success = success;
        this.provider = provider;
        this.messageId = messageId;
        this.error = error;
    }
    // getters & setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
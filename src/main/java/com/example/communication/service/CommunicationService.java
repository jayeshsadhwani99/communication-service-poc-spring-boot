package com.example.communication.service;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;

public interface CommunicationService {
    boolean supports(MessageRequest request);

    MessageResponse sendMessage(MessageRequest request);
}

package com.example.communication.service;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.model.Channel;
import com.example.communication.model.NotificationLog;
import com.example.communication.model.NotificationStatus;
import com.example.communication.repository.NotificationLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * Business logic for creating NotificationLog entries.
 */
@Service
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository repo;
    private final ObjectMapper objectMapper; // auto-configured by Spring Boot

    public void log(MessageRequest req, MessageResponse resp) {
        String notificationId = MDC.get("notificationId");
        NotificationStatus status = resp.isSuccess()
                ? NotificationStatus.SUCCESS
                : NotificationStatus.FAILED;

        String data = resp.getMessageId() != null
                ? resp.getMessageId()
                : resp.getError();

        // Serialize the entire request DTO as JSON
        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            payloadJson = "{\"error\":\"failed to serialize request\"}";
        }

        NotificationLog entry = NotificationLog.builder()
                .notificationId(notificationId)
                .channel(Channel.valueOf(req.getChannel().name()))
                .recipient(req.getTo())
                .status(status)
                .content(req.getContent())
                .data(data)
                .requestPayload(payloadJson)
                .build();

        repo.save(entry);
    }
}

package com.example.communication.service;

import com.example.communication.filter.NotificationIdFilter;
import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.model.Channel;
import com.example.communication.model.NotificationLog;
import com.example.communication.model.NotificationStatus;
import com.example.communication.repository.NotificationLogRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * Business logic for creating NotificationLog entries.
 *
 * Serializes both the incoming request and outgoing response as JSON,
 * maps the boolean success flag to a richer NotificationStatus,
 * and persists the full record in the database.
 */
@Service
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository repo;
    private final ObjectMapper objectMapper; // provided by Spring Boot

    public void log(MessageRequest req, MessageResponse resp) {
        // 1. Retrieve the correlation ID from MDC (set by NotificationIdFilter)
        String notificationId = MDC.get(NotificationIdFilter.MDC_KEY);

        // 2. Map success boolean to NotificationStatus enum
        NotificationStatus status = resp.isSuccess()
                ? NotificationStatus.SUCCESS
                : NotificationStatus.FAILED;

        // 3. Serialize incoming request and outgoing response to JSON
        JsonNode requestJson = objectMapper.valueToTree(req);
        JsonNode responseJson = objectMapper.valueToTree(resp);

        // 4. Build and save the NotificationLog entity
        NotificationLog entry = NotificationLog.builder()
                .notificationId(notificationId)
                .channel(Channel.valueOf(req.getChannel().name()))
                .recipient(req.getTo())
                .status(status)
                .content(req.getContent())
                .requestPayload(requestJson)
                .responsePayload(responseJson)
                .build();

        repo.save(entry);
    }
}

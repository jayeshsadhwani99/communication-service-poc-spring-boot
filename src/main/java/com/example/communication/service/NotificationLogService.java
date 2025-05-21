package com.example.communication.service;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.model.NotificationLog;
import com.example.communication.model.Channel;
import com.example.communication.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Business logic for creating NotificationLog entries.
 */
@Service
@RequiredArgsConstructor
public class NotificationLogService {

    private final NotificationLogRepository repo;

    public void log(MessageRequest req, MessageResponse resp) {
        String notificationId = MDC.get("notificationId");
        NotificationLog entry = NotificationLog.builder()
                .notificationId(notificationId)
                .recipient(req.getTo())
                .content(req.getContent())
                .channel(Channel.valueOf(req.getChannel().name())) // mapping DTO→entity enum
                .success(resp.isSuccess())
                .error(resp.getError())
                .timestamp(LocalDateTime.now())
                .build();
        repo.save(entry); // persists the log
    }
}

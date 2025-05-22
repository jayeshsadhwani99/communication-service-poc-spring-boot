package com.example.communication.controller;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.dispatcher.MessageDispatcher;
import com.example.communication.filter.NotificationIdFilter;
import com.example.communication.service.NotificationLogService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Validated
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageDispatcher dispatcher;
    private final NotificationLogService logService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(
            @Valid @RequestBody MessageRequest request,
            HttpServletResponse httpResponse) {
        log.info("Received message request for {} via {}", request.getTo(), request.getChannel());

        // 1️⃣ Dispatch through the configured providers
        MessageResponse resp = dispatcher.dispatch(request);

        // 2️⃣ Persist the attempt in the DB (NotificationLogService pulls
        // notificationId from MDC)
        logService.log(request, resp);

        // 3️⃣ Fetch the correlation id from MDC and return it in the response header
        String notificationId = MDC.get(NotificationIdFilter.MDC_KEY);
        httpResponse.setHeader(NotificationIdFilter.HEADER, notificationId);

        if (resp.isSuccess()) {
            log.info("Message sent successfully by {}", resp.getProvider());
            return ResponseEntity.ok()
                    .header(NotificationIdFilter.HEADER, notificationId)
                    .body(resp);
        }

        log.error("Message send failed via {}: {}", resp.getProvider(), resp.getError());
        return ResponseEntity.status(500)
                .header(NotificationIdFilter.HEADER, notificationId)
                .body(resp);
    }
}

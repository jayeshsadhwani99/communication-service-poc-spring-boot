package com.example.communication.controller;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.dispatcher.MessageDispatcher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody MessageRequest request) {
        log.info("Received message request for {} via {}", request.getTo(), request.getChannel());
        MessageResponse response = dispatcher.dispatch(request);
        if (response.isSuccess()) {
            log.info("Message sent successfully by {}", response.getProvider());
            return ResponseEntity.ok(response);
        }
        log.error("Message send failed via {}: {}", response.getProvider(), response.getError());
        return ResponseEntity.status(500).body(response);
    }
}

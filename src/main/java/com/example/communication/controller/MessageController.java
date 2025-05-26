package com.example.communication.controller;

import com.example.communication.dto.MessageRequest;
import com.example.communication.dto.MessageResponse;
import com.example.communication.dispatcher.MessageDispatcher;
import com.example.communication.filter.NotificationIdFilter;
import com.example.communication.service.NotificationLogService;
import com.example.communication.template.TemplateService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final TemplateService templateService;
    private final MessageDispatcher dispatcher;
    private final NotificationLogService logService;

    @PostMapping
    public ResponseEntity<MessageResponse> send(
            @Valid @RequestBody MessageRequest req,
            HttpServletResponse httpResponse) {
        // 1. If templating requested, render via facade
        if (req.getTemplateProvider() != null && req.getTemplateName() != null) {
            String body = templateService.render(
                    req.getTemplateProvider(),
                    req.getTemplateName(),
                    req.getTemplateVariables());
            req.setContent(body);
        }

        // 2. Dispatch to WhatsApp/SMS/Email…
        MessageResponse resp = dispatcher.dispatch(req);

        // 3. Log to DB and return X-Notification-Id
        logService.log(req, resp);
        String id = MDC.get(NotificationIdFilter.MDC_KEY);
        httpResponse.setHeader(NotificationIdFilter.HEADER, id);

        return resp.isSuccess()
                ? ResponseEntity.ok().header(NotificationIdFilter.HEADER, id).body(resp)
                : ResponseEntity.status(500).header(NotificationIdFilter.HEADER, id).body(resp);
    }
}

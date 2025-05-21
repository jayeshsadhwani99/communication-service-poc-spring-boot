package com.example.communication.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Generates or propagates a notification_id for every incoming request.
 * - Checks X-Notification-Id header; if missing, creates a new UUID.
 * - Stores in MDC for log correlation.
 * - Adds the same ID to the response header.
 */
@Component
@Slf4j
public class NotificationIdFilter implements Filter {

    public static final String HEADER = "X-Notification-Id";
    public static final String MDC_KEY = "notificationId";

    @Override
    public void doFilter(ServletRequest req,
            ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1. Propagate or generate ID
        String notificationId = request.getHeader(HEADER);
        if (notificationId == null || notificationId.isBlank()) {
            notificationId = UUID.randomUUID().toString(); // UUID generation :contentReference[oaicite:7]{index=7}
        }

        // 2. Put into MDC for consistent logging
        MDC.put(MDC_KEY, notificationId);
        log.debug("Assigned notificationId={}", notificationId);

        try {
            // 3. Return header so clients can track it
            response.setHeader(HEADER, notificationId);
            chain.doFilter(request, response);
        } finally {
            // 4. Clean up to avoid leakage between requests
            MDC.remove(MDC_KEY);
        }
    }
}

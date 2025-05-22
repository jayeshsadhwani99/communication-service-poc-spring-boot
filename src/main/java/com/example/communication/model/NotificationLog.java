package com.example.communication.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Persisted log of every notification attempt.
 */
@Entity
@Table(name = "notification_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Immutable correlation ID, returned to clients in X-Notification-Id */
    @Column(nullable = false, unique = true, updatable = false)
    private String notificationId;

    /** Which channel this log entry is for (WHATSAPP, SMS, EMAIL…) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Channel channel;

    /** Recipient identifier, e.g. phone number or email address */
    @Column(nullable = false, length = 100)
    private String recipient;

    /** Rich status vs. simple boolean */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    /** The original outbound message text */
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Full provider response or error details (JSON/plain text) */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String data;

    /** Entire incoming request payload (serialized JSON) */
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String requestPayload;

    /** When this entry was first created */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** When this entry was last modified */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

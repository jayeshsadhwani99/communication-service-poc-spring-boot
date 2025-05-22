package com.example.communication.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/**
 * Persisted log of every notification attempt.
 *
 * - requestPayload: full incoming DTO JSON (stored as TEXT)
 * - responsePayload: full provider JSON response or error structure (TEXT)
 * - status: richer enum
 * - audit timestamps
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

    /** Correlation ID returned to clients (X-Notification-Id) */
    @Column(nullable = false, unique = true, updatable = false)
    private String notificationId;

    /** Which channel (WHATSAPP, SMS, EMAIL…) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Channel channel;

    /** The intended recipient (phone number or email) */
    @Column(nullable = false, length = 100)
    private String recipient;

    /** Lifecycle status (PENDING, SUCCESS, FAILED, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    /** The original outbound message text or payload */
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Full incoming request DTO, serialized JSON (TEXT) */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "TEXT", nullable = false)
    private JsonNode requestPayload;

    /** Full provider response (success or error) serialized JSON (TEXT) */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "TEXT")
    private JsonNode responsePayload;

    /** When this entry was first created */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** When this entry was last modified */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

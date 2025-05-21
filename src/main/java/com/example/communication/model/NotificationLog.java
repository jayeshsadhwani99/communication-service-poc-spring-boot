package com.example.communication.model;

import jakarta.persistence.*;
import lombok.*;
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

    @Column(nullable = false, unique = true)
    private String notificationId;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Channel channel; // JPA enum mapping

    @Column(nullable = false)
    private boolean success;

    private String error;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}

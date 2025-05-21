package com.example.communication.repository;

import com.example.communication.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Standard Spring Data JPA repository for persisting NotificationLog entries.
 */
@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}

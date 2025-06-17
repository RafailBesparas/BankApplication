package org.example.repository;

import org.example.model.AccountModel;
import org.example.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Interface for managing Notifications. Provides the basic CRUD functionality and custom query methods for user specific notifications
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Retrieves all notifications for a specific user, ordered by timestamp descending
    // Allow recent notifications to be shown first
    List<Notification> findByUserOrderByTimestampDesc(AccountModel user);
}